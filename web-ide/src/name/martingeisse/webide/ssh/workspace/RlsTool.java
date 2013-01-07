/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.PrintWriter;
import java.util.List;

import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.RecursiveResourceOperation;
import name.martingeisse.webide.resources.operation.WorkspaceResourceNotFoundException;

/**
 * Implements the "rls" (recursive "ls") command.
 */
public final class RlsTool implements IWorkspaceTool {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.workspace.IWorkspaceTool#execute(java.lang.String[])
	 */
	@Override
	public void execute(WorkspaceContext context, String[] arguments, final PrintWriter out, final PrintWriter err) {
		if (arguments.length > 0) {
			err.print("this tool does not understand any arguments\r\n");
		}
		RecursiveResourceOperation operation = new RecursiveResourceOperation(context.getCurrentWorkingDirectory()) {
			@Override
			protected void onLevelFetched(List<FetchResourceResult> fetchResults) {
				for (FetchResourceResult result : fetchResults) {
					out.print(result.getPath().toString());
					out.print("\r\n");
				}
			}
		};
		try {
			operation.run();
		} catch (WorkspaceResourceNotFoundException e) {
			err.print(e.getMessage());
			err.print("\r\n");
			return;
		}
	}
	
}
