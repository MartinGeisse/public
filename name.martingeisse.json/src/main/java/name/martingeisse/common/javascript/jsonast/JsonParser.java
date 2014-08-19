/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * Parses a JSON value and produces AST nodes.
 */
public final class JsonParser extends AbstractJsonParser {
	
	/**
	 * the value
	 */
	private JsonAstValue value;
	
	/**
	 * Parses and returns the next value.
	 * @return the parsed value
	 */
	public JsonAstValue parseValue() {
		parse();
		return value;
	}
	
}
