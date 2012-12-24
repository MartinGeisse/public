/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh;

import java.io.PrintWriter;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QFiles;

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
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			for (String filename : query.from(QFiles.files).list(QFiles.files.name)) {
				out.print(filename);
				out.print("\r\n");
			}
		} else {
			out.print("unknown command: " + command + "\r\n");
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
