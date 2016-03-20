/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.xml.content;

import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps a set of known content parsers, indexed by the type
 * of content parsed.
 */
public final class ContentParserRegistry {

	/**
	 * the parsers
	 */
	@SuppressWarnings("rawtypes")
	private final Map<Class, ContentParser> parsers = new HashMap<>();

	/**
	 * Adds a parser to this registry.
	 * 
	 * @param type the type of content the parser must handle
	 * @param parser the parser
	 */
	public <T> void addParser(Class<T> type, ContentParser<T> parser) {
		parsers.put(type, parser);
	}

	/**
	 * Obtains a parser based on the type of parsed content.
	 * 
	 * @param type the type of content the parser must handle
	 * @return the parser, or null if no parser exists for that type
	 */
	@SuppressWarnings("unchecked")
	public <T> ContentParser<T> getParser(Class<T> type) {
		return parsers.get(type);
	}

}
