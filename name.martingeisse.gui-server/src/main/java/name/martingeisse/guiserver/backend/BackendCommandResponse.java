/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.backend;

import java.util.List;

/**
 * A response from the backend containing "commands" to control
 * the GUI server. This kind of response can only be sent by
 * backend URLs for which the GUI server expects them; however,
 * this is the case for most backend URLs. Only cases such as
 * simple HTML includes from the backend do not support command
 * responses.
 */
public class BackendCommandResponse {

	/**
	 * the commands
	 */
	private final List<IBackendCommand> commands;

	/**
	 * Constructor.
	 * @param commands the commands
	 */
	public BackendCommandResponse(List<IBackendCommand> commands) {
		this.commands = commands;
	}

	/**
	 * Getter method for the commands.
	 * @return the commands
	 */
	public List<IBackendCommand> getCommands() {
		return commands;
	}
	
}
