/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.parser;

import java.util.ArrayList;
import java.util.List;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstArray;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState;

/**
 * The state to parse object properties.
 */
public final class ArrayState extends AbstractJsonParserAstBuilderState {

	/**
	 * the elements
	 */
	private final List<JsonAstValue> elements = new ArrayList<>();
	
	/**
	 * Constructor.
	 * @param parentState the parent state
	 * @param line the line where this state starts in the source code
	 * @param column the column where this state starts in the source code 
	 */
	public ArrayState(final AbstractJsonParserAstBuilderState parentState, final int line, final int column) {
		super(parentState, line, column);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parser.AbstractJsonParserAstBuilderState#handleJsonValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue)
	 */
	@Override
	protected void handleJsonValue(JsonAstValue value) {
		elements.add(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parser.AbstractJsonParserAstBuilderState#handleEndArray(int, int)
	 */
	@Override
	protected AbstractJsonParserState handleEndArray(int line, int column) {
		JsonAstValue[] elementArray = elements.toArray(new JsonAstValue[elements.size()]);
		getParentState().handleJsonValue(new JsonAstArray(getLine(), getColumn(), elementArray));
		return getParentState();
	}
	
}
