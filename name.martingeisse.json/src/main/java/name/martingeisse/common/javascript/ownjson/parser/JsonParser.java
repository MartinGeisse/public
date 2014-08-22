/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.parser;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParser;
import name.martingeisse.common.javascript.ownjson.parserbase.JsonLexer;

/**
 * Parses a JSON value and produces AST nodes.
 */
public final class JsonParser extends AbstractJsonParser {

	/**
	 * the result
	 */
	private JsonAstValue result;
	
	/**
	 * Constructor.
	 * @param input the input to parse
	 */
	public JsonParser(String input) {
		super(new JsonLexer(input));
	}
	
	/**
	 * Constructor.
	 * @param lexer the lexical analyzer
	 */
	public JsonParser(JsonLexer lexer) {
		super(lexer);
	}

	/**
	 * Parses and returns the next value.
	 * @return the parsed value
	 */
	public JsonAstValue parseValue() {
		result = null;
		parse(new RootState(this));
		return result;
	}

	/**
	 * Setter method for the result.
	 * @param result the result to set
	 */
	void setResult(JsonAstValue result) {
		this.result = result;
	}
	
}
