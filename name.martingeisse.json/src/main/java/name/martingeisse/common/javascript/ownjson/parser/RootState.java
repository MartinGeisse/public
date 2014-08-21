/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.parser;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState;

/**
 * The "root" state that expects a standalone value and passes it to the {@link JsonParser}.
 */
public final class RootState extends AbstractJsonParserAstBuilderState {

	/**
	 * the parser
	 */
	private final JsonParser parser;

	/**
	 * Constructor.
	 * @param parser the parser
	 */
	RootState(JsonParser parser) {
		this.parser = parser;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parser.AbstractJsonParserAstBuilderState#handleJsonValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue)
	 */
	@Override
	protected AbstractJsonParserState handleJsonValue(JsonAstValue value) {
		parser.setResult(value);
		return this;
	}
	
}
