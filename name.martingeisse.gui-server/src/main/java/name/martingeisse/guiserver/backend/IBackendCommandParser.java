/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.backend;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

/**
 * This parser is used to parse a single command in the backend
 * command response.
 */
public interface IBackendCommandParser {

	/**
	 * Parses a command from its JSON representation.
	 * 
	 * @param json the JSON
	 * @return the parsed backend command
	 */
	public IBackendCommand parseBackendCommand(JsonAnalyzer json);
	
}
