/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.QueryUtil;
import name.martingeisse.webide.entity.QWorkspaceBuildTriggers;
import name.martingeisse.webide.entity.QWorkspaceResourceDeltas;
import name.martingeisse.webide.entity.QWorkspaceTasks;
import name.martingeisse.webide.entity.WorkspaceBuildTriggers;
import name.martingeisse.webide.entity.WorkspaceResourceDeltas;
import name.martingeisse.webide.entity.WorkspaceTasks;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.RecursiveResourceOperation;

import org.apache.log4j.Logger;

import com.mysema.query.sql.dml.SQLDeleteClause;

/**
 * This is the new implementation of the builder thread.
 * It is is triggered by workspace tasks and executes them.
 */
public class BuilderThreadNew {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(BuilderThreadNew.class);

	/**
	 * Creates the builder thread.
	 */
	public static void initialize() {
		new Thread(BuilderThreadNew.class.getSimpleName()) {
			@Override
			public void run() {
				try {
					while (true) {
						for (final WorkspaceTasks task : fetchTasks()) {
							executeTask(task);
						}
						Thread.sleep(1000);
						System.out.println("* ");
					}
				} catch (final Exception e) {
					logger.error("", e);
				}
			}
		}.start();
	}
	
	/**
	 * Fetches pending workspace tasks from the database.
	 */
	private static List<WorkspaceTasks> fetchTasks() {
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

	/**
	 * Executes a single task.
	 */
	private static void executeTask(final WorkspaceTasks task) {
		final String command = task.getCommand();
		if (command.equals("name.martingeisse.webide.resources.consume_deltas")) {
			executeConsumeDeltaTask();
		} else {
			logger.error("Unknown workspace task command: " + command);
		}
	}

	/**
	 * Executes the "consume workspace resource deltas" task.
	 * 
	 * TODO: Either fetch all build triggers in advance (for many deltas)
	 * or select triggers by path prefix (for few deltas). Currently this method
	 * always uses the "many deltas" way (a bit slow if few deltas occur in
	 * a workspace with many triggers).
	 */
	private static void executeConsumeDeltaTask() {
		logger.trace("consuming workspace deltas...");
		final List<WorkspaceBuildTriggers> triggers = fetchAllBuildTriggers();
		for (final WorkspaceResourceDeltas delta : fetchDeltas()) {
			logger.trace("delta: " + delta.getPath() + " (deep: " + delta.getIsDeep() + ")");
			final ResourcePath deltaPath = new ResourcePath(delta.getPath());
			for (final WorkspaceBuildTriggers trigger : triggers) {
				final ResourcePath triggerBasePath = new ResourcePath(trigger.getTriggerBasePath());
				logger.trace("matching against trigger with base path: " + triggerBasePath);
				if (triggerBasePath.isPrefixOf(deltaPath) || (delta.getIsDeep() && deltaPath.isPrefixOf(triggerBasePath))) {
					logger.trace("base path matches");
					if (delta.getIsDeep()) {
						logger.trace("is deep delta, performing recursive match");
						new RecursiveResourceOperation(deltaPath) {
							@Override
							protected void onLevelFetched(final List<FetchResourceResult> fetchResults) {
								for (final FetchResourceResult fetchResult : fetchResults) {
									WorkspaceResourceDeltas subDelta = new WorkspaceResourceDeltas();
									subDelta.setIsDeep(false);
									subDelta.setWorkspaceId(delta.getWorkspaceId());
									subDelta.setPath(fetchResult.getPath().toString());
									logger.trace("matching sub-delta: " + subDelta.getPath());
									matchBuildTriggerPattern(subDelta, trigger);
								}
							}
						};
					} else {
						matchBuildTriggerPattern(delta, trigger);
					}
				}
			}
		}
		logger.trace("finished consuming workspace deltas");
	}

	private static void matchBuildTriggerPattern(final WorkspaceResourceDeltas delta, final WorkspaceBuildTriggers trigger) {
		logger.trace("matching build trigger with pattern " + trigger.getPathPattern() + ", flat delta path: " + delta.getPath());
		if (!Pattern.matches(trigger.getPathPattern(), delta.getPath())) {
			return;
		}
		
		logger.error("*** rebuilding !!!");
		
		/*
		 * TODO
			private String buildscriptPath;
			private Long workspaceBuilderId;
		 */
	}

	/**
	 * Fetches pending workspace resource deltas from the database.
	 */
	private static List<WorkspaceBuildTriggers> fetchAllBuildTriggers() {
		final long workspaceId = 1;
		final QWorkspaceBuildTriggers qtrigger = QWorkspaceBuildTriggers.workspaceBuildTriggers;
		return QueryUtil.fetchMultiple(qtrigger, qtrigger.workspaceId.eq(workspaceId));
	}

	/**
	 * Fetches pending workspace resource deltas from the database.
	 */
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
