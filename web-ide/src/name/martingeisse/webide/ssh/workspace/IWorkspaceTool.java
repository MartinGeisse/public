/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.PrintWriter;

/**
 * Implemented by commands that operate in the workspace context.
 */
public interface IWorkspaceTool {

	/**
	 * Runs this command.
	 * @param context the context
	 * @param arguments command-line arguments
	 * @param out the stdout writer
	 * @param err the stderr writer
	 */
	public void execute(WorkspaceContext context, String[] arguments, final PrintWriter out, final PrintWriter err);
	
}
