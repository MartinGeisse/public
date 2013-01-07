/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.PrintWriter;

import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.ListResourcesOperation;
import name.martingeisse.webide.resources.operation.WorkspaceResourceNotFoundException;

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
		ListResourcesOperation operation = new ListResourcesOperation(context.getCurrentWorkingDirectory());
		try {
			operation.run();
		} catch (WorkspaceResourceNotFoundException e) {
			err.print(e.getMessage());
			err.print("\r\n");
			return;
		}
		for (FetchResourceResult result : operation.getChildren()) {
			out.print(result.getPath().getLastSegment());
			out.print("\r\n");
		}
	}
	
}
