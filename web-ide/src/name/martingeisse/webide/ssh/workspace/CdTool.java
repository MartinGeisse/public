/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.File;
import java.io.PrintWriter;

import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.Workspace;

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
		ResourcePath specifiedTargetPath = new ResourcePath(arguments[0]);
		ResourcePath resolvedTargetPath = context.getCurrentWorkingDirectory().concat(specifiedTargetPath, true).collapse();
		File target = Workspace.map(resolvedTargetPath);
		if (!target.exists()) {
			err.print("No such folder.\r\n");
			return;
		} else if (!target.isDirectory()) {
			err.print("Specified target is not a folder.\r\n");
			return;
		} else {
			context.setCurrentWorkingDirectory(resolvedTargetPath);
		}
	}

}
