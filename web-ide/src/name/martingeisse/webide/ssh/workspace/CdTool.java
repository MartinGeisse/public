/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.PrintWriter;

import name.martingeisse.webide.resources.ResourcePath;

/**
 * Implements the "cd" command.
 */
public final class CdTool implements IWorkspaceTool {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.workspace.IWorkspaceTool#execute(name.martingeisse.webide.ssh.workspace.WorkspaceContext, java.lang.String[], java.io.PrintWriter, java.io.PrintWriter)
	 */
	@Override
	public void execute(final WorkspaceContext context, final String[] arguments, final PrintWriter out, final PrintWriter err) {
		if (arguments.length != 1) {
			err.println("usage: cd <path>\r\n");
			return;
		}
		ResourcePath targetPath;
		try {
			targetPath = new ResourcePath(arguments[0]);
			context.setCurrentWorkingDirectory(context.getCurrentWorkingDirectory().concat(targetPath, true).collapse());
		} catch (Exception e) {
			err.print(e.getMessage() + "\r\n");
			return;
		}
	}

}
