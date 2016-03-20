/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
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
	 * the startLine
	 */
	private final int startLine;
	
	/**
	 * the startLine
	 */
	private final int startColumn;

	/**
	 * Constructor.
	 * @param parentState the parent state
	 * @param startLine the line where this state starts in the source code
	 * @param startColumn the column where this state starts in the source code 
	 */
	public AbstractJsonParserAstBuilderState(final AbstractJsonParserAstBuilderState parentState, final int startLine, final int startColumn) {
		this.parentState = parentState;
		this.startLine = startLine;
		this.startColumn = startColumn;
	}
	
	/**
	 * Getter method for the parentState.
	 * @return the parentState
	 */
	AbstractJsonParserAstBuilderState getParentState() {
		return parentState;
	}
	
	/**
	 * Getter method for the startLine.
	 * @return the startLine
	 */
	int getStartLine() {
		return startLine;
	}

	/**
	 * Getter method for the startColumn.
	 * @return the startColumn
	 */
	int getStartColumn() {
		return startColumn;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleNullValue(int, int, int, int)
	 */
	@Override
	protected AbstractJsonParserState handleNullValue(int startLine, int startColumn, int endLine, int endColumn) {
		handleJsonValue(new JsonAstNull(startLine, startColumn, endLine, endColumn));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleBooleanValue(int, int, int, int, boolean)
	 */
	@Override
	protected AbstractJsonParserState handleBooleanValue(int startLine, int startColumn, int endLine, int endColumn, boolean value) {
		handleJsonValue(new JsonAstBoolean(startLine, startColumn, endLine, endColumn, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleIntegerValue(int, int, int, int, long)
	 */
	@Override
	protected AbstractJsonParserState handleIntegerValue(int startLine, int startColumn, int endLine, int endColumn, long value) {
		handleJsonValue(new JsonAstInteger(startLine, startColumn, endLine, endColumn, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleFloatingPointValue(int, int, int, int, double)
	 */
	@Override
	protected AbstractJsonParserState handleFloatingPointValue(int startLine, int startColumn, int endLine, int endColumn, double value) {
		handleJsonValue(new JsonAstFloat(startLine, startColumn, endLine, endColumn, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleStringValue(int, int, int, int, java.lang.String)
	 */
	@Override
	protected AbstractJsonParserState handleStringValue(int startLine, int startColumn, int endLine, int endColumn, String value) {
		handleJsonValue(new JsonAstString(startLine, startColumn, endLine, endColumn, value));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleBeginArray(int, int, int, int)
	 */
	@Override
	protected AbstractJsonParserState handleBeginArray(int startLine, int startColumn, int endLine, int endColumn) {
		return new ArrayState(this, startLine, startColumn);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleEndArray(int, int, int, int)
	 */
	@Override
	protected AbstractJsonParserState handleEndArray(int startLine, int startColumn, int endLine, int endColumn) {
		throw new IllegalStateException("cannot handle end-of-array in state " + this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleBeginObject(int, int, int, int)
	 */
	@Override
	protected AbstractJsonParserState handleBeginObject(int startLine, int startColumn, int endLine, int endColumn) {
		return new ObjectState(this, startLine, startColumn);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleObjectPropertyName(int, int, int, int, java.lang.String)
	 */
	@Override
	protected AbstractJsonParserState handleObjectPropertyName(int startLine, int startColumn, int endLine, int endColumn, String name) {
		throw new IllegalStateException("cannot handle object property in state " + this);
	};

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState#handleEndObject(int, int, int, int)
	 */
	@Override
	protected AbstractJsonParserState handleEndObject(int startLine, int startColumn, int endLine, int endColumn) {
		throw new IllegalStateException("cannot handle end-of-object in state " + this);
	}

	/**
	 * Base method to handle AST value nodes.
	 * @param value the value node
	 */
	protected abstract void handleJsonValue(JsonAstValue value);

}
