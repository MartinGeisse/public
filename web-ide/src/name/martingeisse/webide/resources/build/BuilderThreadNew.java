/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.QueryUtil;
import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.entity.QWorkspaceBuildTriggers;
import name.martingeisse.webide.entity.QWorkspaceBuilders;
import name.martingeisse.webide.entity.QWorkspaceResourceDeltas;
import name.martingeisse.webide.entity.QWorkspaceTasks;
import name.martingeisse.webide.entity.WorkspaceBuildTriggers;
import name.martingeisse.webide.entity.WorkspaceBuilders;
import name.martingeisse.webide.entity.WorkspaceResourceDeltas;
import name.martingeisse.webide.entity.WorkspaceTasks;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.ResourcePath;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.mysema.query.sql.dml.SQLDeleteClause;

/**
 * This is the new implementation of the builder thread.
 * It is is triggered by workspace tasks and executes them.
 * Consuming resource deltas occurs in the "consume deltas" task.
 */
public class BuilderThreadNew {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(BuilderThreadNew.class);

	public static void createBuilderThread() {
		new Thread(BuilderThreadNew.class.getSimpleName()) {
			@Override
			public void run() {
				while (true) {
					try {
						for (final WorkspaceTasks task : fetchPendingTasks()) {
							executeTask(task);
						}
						Thread.sleep(1000);
					} catch (final Exception e) {
						logger.error("", e);
					}
				}
			}
		}.start();
	}
	
	private static List<WorkspaceTasks> fetchPendingTasks() {
		final List<WorkspaceTasks> tasks = QueryUtil.fetchAll(QWorkspaceTasks.workspaceTasks);
		final List<Long> taskIds = new ArrayList<Long>();
		for (final WorkspaceTasks task : tasks) {
			taskIds.add(task.getId());
		}
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceTasks.workspaceTasks);
		delete.where(QWorkspaceTasks.workspaceTasks.id.in(taskIds));
		delete.execute();
		return tasks;
	}

	private static void executeTask(final WorkspaceTasks task) {
		final String command = task.getCommand();
		if (command.equals("name.martingeisse.webide.resources.consume_deltas")) {
			executeConsumeDeltaTask(task.getWorkspaceId());
		} else {
			logger.error("Unknown workspace task command: " + command);
		}
	}

	/* TODO: Dynamically either fetch all build triggers in advance (for many deltas)
	 * or select triggers by path prefix (for few deltas).
	 */
	private static void executeConsumeDeltaTask(long workspaceId) {
		logger.trace("consuming workspace deltas...");
		
		// fetch deltas
		final List<WorkspaceResourceDeltas> rawDeltas = fetchDeltas();
		final List<BuilderResourceDelta> allDeltas = new ArrayList<BuilderResourceDelta>();
		for (WorkspaceResourceDeltas rawDelta : rawDeltas) {
			allDeltas.add(new BuilderResourceDelta(rawDelta));
		}
		
		// invoke listeners first
		WorkspaceListenerRegistry.onWorkspaceChange(workspaceId, allDeltas);
		
		// then invoke builders
		final List<WorkspaceBuildTriggers> triggers = fetchAllBuildTriggers(workspaceId);
		final Map<Long, WorkspaceBuilders> workspaceBuildersById = fetchWorkspaceBuildersByIdForBuildTriggers(triggers);
		for (WorkspaceBuildTriggers trigger : triggers) {
			logger.trace("collecting deltas for trigger " + trigger.getBuildscriptPath() + ", base path " + trigger.getTriggerBasePath());
			final ResourcePath triggerBasePath = new ResourcePath(trigger.getTriggerBasePath());
			Set<BuilderResourceDelta> builderDeltas = new HashSet<BuilderResourceDelta>();
			for (final BuilderResourceDelta delta : allDeltas) {
				logger.trace("delta: " + delta.getPath() + " (deep: " + delta.isDeep() + ")");
				final ResourcePath deltaPath = delta.getPath();
				if (triggerBasePath.isPrefixOf(deltaPath) || (delta.isDeep() && deltaPath.isPrefixOf(triggerBasePath))) {
					logger.trace("base path matches");
					if (delta.isDeep()) {
						logger.trace("deep delta -- skipping path pattern check");
						builderDeltas.add(delta);
					} else {
						logger.trace("matching build trigger with pattern " + trigger.getPathPattern() + ", delta path: " + deltaPath);
						boolean result = Pattern.matches(trigger.getPathPattern(), delta.getPath().toString());
						if (result) {
							logger.error("pattern matched, remembering delta for builder invocation");						
							builderDeltas.add(delta);
						} else {
							logger.error("pattern did not match");						
						}
					}
				} else {
					logger.trace("base path does not match");
				}
			}
			if (builderDeltas.isEmpty()) {
				logger.trace("no deltas left for this build trigger");
				continue;
			}
			logger.trace("Paths for builder " + trigger.getWorkspaceBuilderId() + ", trigger " + trigger.getBuildscriptPath() + ": " + StringUtils.join(builderDeltas, ", "));
			WorkspaceBuilders workspaceBuilder = workspaceBuildersById.get(trigger.getWorkspaceBuilderId());
			PluginBundleHandle bundleHandle = new PluginBundleHandle(workspaceBuilder.getPluginBundleId());
			IBuilder builder;
			try {
				logger.trace("creating builder " + workspaceBuilder.getBuilderClass() + " from plugin bundle " + workspaceBuilder.getPluginBundleId());
				builder = bundleHandle.createObject(IBuilder.class, workspaceBuilder.getBuilderClass());
				logger.trace("builder created: " + builder);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			logger.trace("starting incremental build step...");
			builder.incrementalBuild(workspaceId, JsonAnalyzer.parse(trigger.getDescriptor()), builderDeltas);
			logger.trace("incremental build step finished");
		}
		
		logger.trace("finished consuming workspace deltas");
	}

	private static List<WorkspaceBuildTriggers> fetchAllBuildTriggers(long workspaceId) {
		final QWorkspaceBuildTriggers qtrigger = QWorkspaceBuildTriggers.workspaceBuildTriggers;
		return QueryUtil.fetchMultiple(qtrigger, qtrigger.workspaceId.eq(workspaceId));
	}
	
	private static Map<Long, WorkspaceBuilders> fetchWorkspaceBuildersByIdForBuildTriggers(Iterable<WorkspaceBuildTriggers> triggers) {
		List<Long> workspaceBuilderIds = new ArrayList<Long>();
		for (WorkspaceBuildTriggers trigger : triggers) {
			workspaceBuilderIds.add(trigger.getWorkspaceBuilderId());
		}
		QWorkspaceBuilders qbuilder = QWorkspaceBuilders.workspaceBuilders;
		return QueryUtil.from(qbuilder).where(qbuilder.id.in(workspaceBuilderIds)).map(qbuilder.id, qbuilder);
	}
	
	private static List<WorkspaceResourceDeltas> fetchDeltas() {
		final List<WorkspaceResourceDeltas> deltas = QueryUtil.fetchAll(QWorkspaceResourceDeltas.workspaceResourceDeltas);
		final List<Long> deltaIds = new ArrayList<Long>();
		for (final WorkspaceResourceDeltas delta : deltas) {
			deltaIds.add(delta.getId());
		}
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceResourceDeltas.workspaceResourceDeltas);
		delete.where(QWorkspaceResourceDeltas.workspaceResourceDeltas.id.in(deltaIds));
		delete.execute();
		return deltas;
	}

}
