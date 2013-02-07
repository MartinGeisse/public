/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.QueryUtil;
import name.martingeisse.webide.entity.QWorkspaceResourceDeltas;
import name.martingeisse.webide.entity.QWorkspaceTasks;
import name.martingeisse.webide.entity.WorkspaceResourceDeltas;
import name.martingeisse.webide.entity.WorkspaceTasks;

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
	 * static initializer
	 */
	static {
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						for (WorkspaceTasks task : fetchTasks()) {
							executeTask(task);
						}
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {
				}
			}
		}.start();
	}

	/**
	 * Fetches pending workspace tasks from the database.
	 */
	private static List<WorkspaceTasks> fetchTasks() {
		List<WorkspaceTasks> tasks = QueryUtil.fetchAll(QWorkspaceTasks.workspaceTasks);
		List<Long> taskIds = new ArrayList<Long>();
		for (WorkspaceTasks task : tasks) {
			taskIds.add(task.getId());
		}
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceTasks.workspaceTasks);
		delete.where(QWorkspaceTasks.workspaceTasks.id.in(taskIds));
		delete.execute();
		return tasks;
	}
	
	/**
	 * Executes a single task.
	 */
	private static void executeTask(WorkspaceTasks task) {
		String command = task.getCommand();
		if (command.equals("name.martingeisse.webide.resources.consume_deltas")) {
			executeConsumeDeltaTask();
		} else {
			logger.error("Unknown workspace task command: " + command);
		}
	}
	
	/**
	 * Executes the "consume workspace resource deltas" task.
	 */
	private static void executeConsumeDeltaTask() {
		for (WorkspaceResourceDeltas delta : fetchDeltas()) {
			
		}
	}

	/**
	 * Fetches pending workspace resource deltas from the database.
	 */
	private static List<WorkspaceResourceDeltas> fetchDeltas() {
		List<WorkspaceResourceDeltas> deltas = QueryUtil.fetchAll(QWorkspaceResourceDeltas.workspaceResourceDeltas);
		List<Long> deltaIds = new ArrayList<Long>();
		for (WorkspaceResourceDeltas delta : deltas) {
			deltaIds.add(delta.getId());
		}
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QWorkspaceResourceDeltas.workspaceResourceDeltas);
		delete.where(QWorkspaceResourceDeltas.workspaceResourceDeltas.id.in(deltaIds));
		delete.execute();
		return deltas;
	}
	
}
