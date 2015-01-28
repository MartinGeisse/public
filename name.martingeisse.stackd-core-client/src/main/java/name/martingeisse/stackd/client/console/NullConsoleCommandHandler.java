/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.console;

/**
 * Ignores all commands.
 */
public final class NullConsoleCommandHandler implements IConsoleCommandHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.console.ICommandHandler#handleCommand(name.martingeisse.stackd.client.console.Console, java.lang.String, java.lang.String[])
	 */
	@Override
	public void handleCommand(final Console console, final String command, final String[] args) {
	}

}
