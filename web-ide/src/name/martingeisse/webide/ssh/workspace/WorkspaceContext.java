/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh.workspace;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.ssh.IShellContext;

/**
 * The default shell context used for SSH access.
 */
public class WorkspaceContext implements IShellContext {

	/**
	 * the tools
	 */
	private final Map<String, IWorkspaceTool> tools;

	/**
	 * the currentWorkingDirectory
	 */
	private ResourcePath currentWorkingDirectory;

	/**
	 * Constructor.
	 */
	public WorkspaceContext() {
		this.tools = new HashMap<String, IWorkspaceTool>();
		this.tools.put("ls", new LsTool());
		this.tools.put("rls", new RlsTool());
		this.tools.put("cd", new CdTool());
		this.tools.put("pwd", new PwdTool());
		this.currentWorkingDirectory = new ResourcePath("/");
	}

	/**
	 * Getter method for the currentWorkingDirectory.
	 * @return the currentWorkingDirectory
	 */
	public ResourcePath getCurrentWorkingDirectory() {
		return currentWorkingDirectory;
	}

	/**
	 * Setter method for the currentWorkingDirectory.
	 * @param currentWorkingDirectory the currentWorkingDirectory to set
	 */
	public void setCurrentWorkingDirectory(final ResourcePath currentWorkingDirectory) {
		this.currentWorkingDirectory = currentWorkingDirectory;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#printPrompt(java.io.PrintWriter)
	 */
	@Override
	public void printPrompt(final PrintWriter w) {
		w.print(currentWorkingDirectory.toString());
		w.print("> ");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#autocomplete(java.lang.StringBuilder)
	 */
	@Override
	public AutocompleteStatus autocomplete(final StringBuilder commandLineBuilder) {
		return AutocompleteStatus.UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#suggest(java.lang.StringBuilder, java.io.PrintWriter)
	 */
	@Override
	public void suggest(final StringBuilder commandLineBuilder, final PrintWriter out) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#execute(java.lang.String, java.io.PrintWriter, java.io.PrintWriter)
	 */
	@Override
	public ExecuteStatus execute(final String command, final PrintWriter out, final PrintWriter err) {

		// parse the command
		final String[] commandSegments = command.trim().split("\\s+");
		if (commandSegments.length == 0 || commandSegments[0].isEmpty()) {
			return ExecuteStatus.OK;
		}
		final String verb = commandSegments[0];
		final String[] arguments = Arrays.copyOfRange(commandSegments, 1, commandSegments.length);

		// special commands
		if (verb.equals("exit")) {
			if (arguments.length == 0) {
				return ExecuteStatus.TERMINATE;
			} else {
				err.print("the 'exit' command does not understand any arguments\r\n");
				return ExecuteStatus.OK;
			}
		}

		// regular commands
		final IWorkspaceTool tool = tools.get(verb);
		if (tool != null) {
			try {
				tool.execute(this, arguments, out, err);
			} catch (Exception e) {
				err.print(e.getMessage() + "\r\n");
				e.printStackTrace(System.out);
			}
		} else {
			err.print("unknown command: " + verb + "\r\n");
		}
		return ExecuteStatus.OK;

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#getSubContext()
	 */
	@Override
	public IShellContext getSubContext() {
		return null;
	}

}
