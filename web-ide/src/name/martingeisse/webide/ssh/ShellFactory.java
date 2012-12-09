/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;

/**
 * The factory for {@link ShellCommand} objects.
 */
public class ShellFactory implements Factory<Command> {

	/* (non-Javadoc)
	 * @see org.apache.sshd.common.Factory#create()
	 */
	@Override
	public Command create() {
		return new ShellCommand();
	}

}
