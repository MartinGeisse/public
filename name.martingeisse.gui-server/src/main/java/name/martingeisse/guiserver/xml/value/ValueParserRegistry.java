/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.value;

import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps a set of known value parsers, indexed by the type
 * of values parsed.
 */
public final class ValueParserRegistry {

	/**
	 * the parsers
	 */
	@SuppressWarnings("rawtypes")
	private final Map<Class, ValueParser> parsers = new HashMap<>();

	/**
	 * Adds a parser to this registry.
	 * 
	 * @param type the type of values the parser must handle
	 * @param parser the parser
	 */
	public <T> void addParser(Class<T> type, ValueParser<T> parser) {
		parsers.put(type, parser);
	}

	/**
	 * Obtains a parser based on the type of parsed values.
	 * 
	 * @param type the type of values the parser must handle
	 * @return the parser, or null if no parser exists for that type
	 */
	@SuppressWarnings("unchecked")
	public <T> ValueParser<T> getParser(Class<T> type) {
		return parsers.get(type);
	}

}
