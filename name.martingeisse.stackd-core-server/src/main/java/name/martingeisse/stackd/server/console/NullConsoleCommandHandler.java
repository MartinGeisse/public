/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.console;

import name.martingeisse.stackd.server.network.StackdSession;

/**
 * Ignores all commands.
 * 
 * @param <S> the session type
 */
public final class NullConsoleCommandHandler<S extends StackdSession> implements IConsoleCommandHandler<S> {

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.server.console.ICommandHandler#handleCommand(name.martingeisse.stackd.server.network.StackdSession, java.lang.String, java.lang.String[])
	 */
	@Override
	public void handleCommand(S session, String command, String[] args) {
	}

}
