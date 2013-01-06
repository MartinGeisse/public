/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.PrintWriter;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.resources.ResourceType;

import com.mysema.query.sql.SQLQuery;

/**
 * Implements the "ls" command.
 */
public final class LsTool implements IWorkspaceTool {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.workspace.IWorkspaceTool#execute(java.lang.String[])
	 */
	@Override
	public void execute(WorkspaceContext context, String[] arguments, final PrintWriter out, final PrintWriter err) {
		if (arguments.length > 0) {
			err.print("this tool does not understand any arguments\r\n");
		}
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.type.eq(ResourceType.FILE.name()));
		for (final String filename : query.list(QWorkspaceResources.workspaceResources.name)) {
			out.print(filename);
			out.print("\r\n");
		}
	}
	
}
