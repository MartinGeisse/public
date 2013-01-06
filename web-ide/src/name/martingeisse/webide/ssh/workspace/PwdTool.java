/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.PrintWriter;

/**
 * Implements the "pwd" command.
 */
public final class PwdTool implements IWorkspaceTool {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.workspace.IWorkspaceTool#execute(name.martingeisse.webide.ssh.workspace.WorkspaceContext, java.lang.String[], java.io.PrintWriter, java.io.PrintWriter)
	 */
	@Override
	public void execute(final WorkspaceContext context, final String[] arguments, final PrintWriter out, final PrintWriter err) {
		out.print(context.getCurrentWorkingDirectory().toString());
		out.print("\r\n");
	}

}
