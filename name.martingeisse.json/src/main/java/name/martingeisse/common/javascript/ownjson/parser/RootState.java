/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.parser;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;

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
		super(null, 0, 0);
		this.parser = parser;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parser.AbstractJsonParserAstBuilderState#handleJsonValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue)
	 */
	@Override
	protected void handleJsonValue(JsonAstValue value) {
		parser.setResult(value);
	}
	
}
