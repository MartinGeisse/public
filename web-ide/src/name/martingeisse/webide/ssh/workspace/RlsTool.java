/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.File;
import java.io.PrintWriter;

import name.martingeisse.webide.resources.RecursiveResourceOperation;
import name.martingeisse.webide.resources.ResourcePath;

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
		new RecursiveResourceOperation() {
			@Override
			protected void handle(ResourcePath path, File resource) {
				out.print(path);
				out.print("\r\n");
				super.handle(resource);
			};
		}.handle(context.getCurrentWorkingDirectory());
	}
	
}
