/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.server.console;

import name.martingeisse.stackd.server.network.StackdSession;

/**
 * The network code delegates to this object to handle incoming console commands.
 * 
 * @param <S> the session type
 */
public interface IConsoleCommandHandler<S extends StackdSession> {

	/**
	 * Handles a command.
	 * 
	 * @param session the session of the client sending the command
	 * @param command the command
	 * @param args arguments
	 */
	public void handleCommand(S session, String command, String[] args);
	
}
