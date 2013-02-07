/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResourceDeltas;
import name.martingeisse.webide.entity.QWorkspaceTasks;
import name.martingeisse.webide.entity.QWorkspaces;
import name.martingeisse.webide.resources.ResourcePath;

import org.apache.log4j.Logger;

import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * Utility methods used to generate resource deltas.
 * 
 * Note: This class used to have a separate method that creates deltas, but
 * does not create a delta consumption task. This method was intended for
 * the case that more deltas will follow, but is dangerous: At the time
 * the "last" deltas arrive and the caller intends to add a consumption
 * task, the set of "last" deltas might be empty and so no consumption
 * task is added, despite there being deltas from previous calls! Instead,
 * this class now still skips the task if no deltas are being added, but
 * always adds a task if at least one delta is being added, even if more
 * deltas will follow. The consumption task itself should merge with
 * subsequent tasks.
 */
class WorkspaceResourceDeltaUtil {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(WorkspaceResourceDeltaUtil.class);
	
	/**
	 * Prevent instantiation.
	 */
	private WorkspaceResourceDeltaUtil() {
	}
	
	/**
	 * Generates resource deltas for the specified paths as well as a delta consumption task
	 * and sets the workspace to "building" state.
	 * 
	 * @param callerForLogging the caller of this method (simple string used for logging)
	 * @param paths the paths
	 */
	public static void generateDeltas(String callerForLogging, ResourcePath... paths) {
		long workspaceId = 1;
		
		logger.trace(callerForLogging + ": creating resource deltas ...");
		for (ResourcePath path : paths) {
			logger.trace(callerForLogging + ": creating resource delta for path: " + path);
			SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceResourceDeltas.workspaceResourceDeltas);
			insert.set(QWorkspaceResourceDeltas.workspaceResourceDeltas.workspaceId, workspaceId);
			insert.set(QWorkspaceResourceDeltas.workspaceResourceDeltas.path, path.toString());
			insert.execute();
		}
		logger.trace(callerForLogging + ": finished creating resource deltas.");

		if (paths.length > 0) {
			logger.trace(callerForLogging + ": creating resource delta consumption task ...");
			SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QWorkspaceTasks.workspaceTasks);
			insert.set(QWorkspaceResourceDeltas.workspaceResourceDeltas.workspaceId, workspaceId);
			insert.set(QWorkspaceTasks.workspaceTasks.command, "name.martingeisse.webide.resources.consume_deltas");
			insert.execute();
			logger.trace(callerForLogging + ": finished creating resource delta consumption task, will set the workspace to 'building' state.");
			SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QWorkspaces.workspaces);
			update.where(QWorkspaces.workspaces.id.eq(workspaceId));
			update.set(QWorkspaces.workspaces.isBuilding, true);
			update.execute();
			logger.trace(callerForLogging + ": finished setting the workspace to 'building' state.");
		} else {
			logger.trace(callerForLogging + ": resource delta consumption task and 'building' state not needed because no deltas were generated.");
		}
	}
	
}
