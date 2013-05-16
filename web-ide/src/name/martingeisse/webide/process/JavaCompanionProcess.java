/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process;

import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.commons.exec.CommandLine;

/**
 * TODO: document me
 *
 */
public class JavaCompanionProcess extends CompanionProcess {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.process.CompanionProcess#buildCommandLine()
	 */
	@Override
	protected CommandLine buildCommandLine() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.process.CompanionProcess#doSendEvent(name.martingeisse.webide.ipc.IpcEvent)
	 */
	@Override
	protected void doSendEvent(IpcEvent event) {
	}
	
}
