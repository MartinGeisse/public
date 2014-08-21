/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.parser;

import name.martingeisse.common.javascript.ownjson.ast.JsonAstBoolean;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstFloat;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstInteger;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstNull;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstString;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState;

/**
 * Base class for AST-building JSON parser states.
 */
public abstract class AbstractJsonParserAstBuilderState extends AbstractJsonParserState {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleNullValue(int, int)
	 */
	@Override
	protected final AbstractJsonParserState handleNullValue(int line, int column) {
		return handleJsonValue(new JsonAstNull(line, column));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleBooleanValue(int, int, boolean)
	 */
	@Override
	protected final AbstractJsonParserState handleBooleanValue(int line, int column, boolean value) {
		return handleJsonValue(new JsonAstBoolean(line, column, value));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleIntegerValue(int, int, long)
	 */
	@Override
	protected final AbstractJsonParserState handleIntegerValue(int line, int column, long value) {
		return handleJsonValue(new JsonAstInteger(line, column, value));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleFloatingPointValue(int, int, double)
	 */
	@Override
	protected final AbstractJsonParserState handleFloatingPointValue(int line, int column, double value) {
		return handleJsonValue(new JsonAstFloat(line, column, value));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleStringValue(int, int, java.lang.String)
	 */
	@Override
	protected final AbstractJsonParserState handleStringValue(int line, int column, String value) {
		return handleJsonValue(new JsonAstString(line, column, value));
	}

	/**
	 * Base method to handle AST value nodes.
	 * @param value the value node
	 * @return the next state
	 */
	protected abstract AbstractJsonParserState handleJsonValue(JsonAstValue value);
	
	
}
