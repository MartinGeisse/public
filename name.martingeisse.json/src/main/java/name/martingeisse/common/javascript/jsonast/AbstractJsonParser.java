/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * base class for the JSON parser. This class implements the parsing logic
 * but does not create AST nodes. It can be used as the basis for SAJ-like
 * parsers.
 */
public abstract class AbstractJsonParser {

	/**
	 * the lexer
	 */
	private final JsonLexer lexer;

	/**
	 * Constructor.
	 * @param lexer the lexical analyzer
	 */
	public AbstractJsonParser(JsonLexer lexer) {
		this.lexer = lexer;
	}

	/**
	 * Parses the input and invokes event handler methods.
	 */
	protected final void parse() {

	}

}
