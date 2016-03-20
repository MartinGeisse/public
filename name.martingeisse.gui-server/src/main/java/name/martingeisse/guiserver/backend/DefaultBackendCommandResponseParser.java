/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.backend;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of {@link IBackendCommandResponseParser}.
 */
public class DefaultBackendCommandResponseParser extends AbstractBackendCommandResponseParser {

	/**
	 * Constructor.
	 */
	public DefaultBackendCommandResponseParser() {
		super(createMutableDefaultCommandParserMap());
	}

	/**
	 * Creates a map containing the default command parsers, and returns it as a
	 * still-mutable map, so the caller can add parsers.
	 * 
	 * @return the parser map
	 */
	public static Map<String, IBackendCommandParser> createMutableDefaultCommandParserMap() {
		Map<String, IBackendCommandParser> map = new HashMap<>();
		// TODO
		return map;
	}
	
}
