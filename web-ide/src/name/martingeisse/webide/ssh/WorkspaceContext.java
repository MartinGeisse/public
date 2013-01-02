/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh;

import java.io.PrintWriter;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.resources.ResourceType;

import com.mysema.query.sql.SQLQuery;

/**
 * The default shell context used for SSH access.
 */
public class WorkspaceContext implements IShellContext {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#autocomplete(java.lang.StringBuilder)
	 */
	@Override
	public AutocompleteStatus autocomplete(StringBuilder commandLineBuilder) {
		return AutocompleteStatus.UNKNOWN;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#suggest(java.lang.StringBuilder, java.io.PrintWriter)
	 */
	@Override
	public void suggest(StringBuilder commandLineBuilder, PrintWriter out) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#execute(java.lang.String, java.io.PrintWriter, java.io.PrintWriter)
	 */
	@Override
	public ExecuteStatus execute(String command, PrintWriter out, PrintWriter err) {
		
		// special commands
		if (command.isEmpty()) {
			return ExecuteStatus.OK;
		}
		if (command.equals("exit")) {
			return ExecuteStatus.TERMINATE;
		}

		// regular commands
		if (command.equals("ls")) {
			for (String filename : getFilenames()) {
				out.print(filename);
				out.print("\r\n");
			}
		} else {
			out.print("unknown command: " + command + "\r\n");
		}
		
		return ExecuteStatus.OK;
	}

	/**
	 * Returns the file names.
	 * @return the file names.
	 */
	private List<String> getFilenames() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.type.eq(ResourceType.FILE.name()));
		return query.list(QWorkspaceResources.workspaceResources.name);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.ssh.IShellContext#getSubContext()
	 */
	@Override
	public IShellContext getSubContext() {
		return null;
	}

}
