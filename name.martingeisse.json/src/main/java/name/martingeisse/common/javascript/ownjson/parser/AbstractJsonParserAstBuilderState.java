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

	/**
	 * the parentState
	 */
	private final AbstractJsonParserAstBuilderState parentState;
	
	/**
	 * the line
	 */
	private final int line;
	
	/**
	 * the column
	 */
	private final int column;

	/**
	 * Constructor.
	 * @param parentState the parent state
	 * @param line the line where this state starts in the source code
	 * @param column the column where this state starts in the source code 
	 */
	public AbstractJsonParserAstBuilderState(final AbstractJsonParserAstBuilderState parentState, final int line, final int column) {
		this.parentState = parentState;
		this.line = line;
		this.column = column;
	}
	
	/**
	 * Getter method for the parentState.
	 * @return the parentState
	 */
	AbstractJsonParserAstBuilderState getParentState() {
		return parentState;
	}
	
	/**
	 * Getter method for the line.
	 * @return the line
	 */
	int getLine() {
		return line;
	}
	
	/**
	 * Getter method for the column.
	 * @return the column
	 */
	int getColumn() {
		return column;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleNullValue(int, int)
	 */
	@Override
	protected final AbstractJsonParserState handleNullValue(final int line, final int column) {
		handleJsonValue(new JsonAstNull(line, column));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleBooleanValue(int, int, boolean)
	 */
	@Override
	protected final AbstractJsonParserState handleBooleanValue(final int line, final int column, final boolean value) {
		handleJsonValue(new JsonAstBoolean(line, column, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleIntegerValue(int, int, long)
	 */
	@Override
	protected final AbstractJsonParserState handleIntegerValue(final int line, final int column, final long value) {
		handleJsonValue(new JsonAstInteger(line, column, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleFloatingPointValue(int, int, double)
	 */
	@Override
	protected final AbstractJsonParserState handleFloatingPointValue(final int line, final int column, final double value) {
		handleJsonValue(new JsonAstFloat(line, column, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleStringValue(int, int, java.lang.String)
	 */
	@Override
	protected final AbstractJsonParserState handleStringValue(final int line, final int column, final String value) {
		handleJsonValue(new JsonAstString(line, column, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleBeginArray(int, int)
	 */
	@Override
	protected AbstractJsonParserState handleBeginArray(int line, int column) {
		return new ArrayState(this, line, column);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleEndArray(int, int)
	 */
	@Override
	protected AbstractJsonParserState handleEndArray(int line, int column) {
		throw new IllegalStateException("cannot handle end-of-array in state " + this);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleBeginObject(int, int)
	 */
	@Override
	protected AbstractJsonParserState handleBeginObject(int line, int column) {
		return new ObjectState(this, line, column);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleObjectPropertyName(int, int, java.lang.String)
	 */
	@Override
	protected AbstractJsonParserState handleObjectPropertyName(final int line, final int column, final String name) {
		throw new IllegalStateException("cannot handle object property in state " + this);
	};
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleEndObject(int, int)
	 */
	@Override
	protected AbstractJsonParserState handleEndObject(int line, int column) {
		throw new IllegalStateException("cannot handle end-of-object in state " + this);
	}

	/**
	 * Base method to handle AST value nodes.
	 * @param value the value node
	 */
	protected abstract void handleJsonValue(JsonAstValue value);

}
