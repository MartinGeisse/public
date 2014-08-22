/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.parser;

import java.util.ArrayList;
import java.util.List;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObjectProperty;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstString;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;
import name.martingeisse.common.javascript.ownjson.parserbase.AbstractJsonParserState;

/**
 * The state to parse object properties.
 */
public final class ObjectState extends AbstractJsonParserAstBuilderState {

	/**
	 * the properties
	 */
	private final List<JsonAstObjectProperty> properties = new ArrayList<>();

	/**
	 * the propertyName
	 */
	private JsonAstString propertyName;

	/**
	 * Constructor.
	 * @param parentState the parent state
	 * @param startLine the line where this state starts in the source code
	 * @param startColumn the column where this state starts in the source code 
	 */
	public ObjectState(final AbstractJsonParserAstBuilderState parentState, final int startLine, final int startColumn) {
		super(parentState, startLine, startColumn);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parser.AbstractJsonParserAstBuilderState#handleObjectPropertyName(int, int, int, int, java.lang.String)
	 */
	@Override
	protected AbstractJsonParserState handleObjectPropertyName(int startLine, int startColumn, int endLine, int endColumn, String name) {
		this.propertyName = new JsonAstString(startLine, startColumn, endLine, endColumn, name);
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parser.AbstractJsonParserAstBuilderState#handleJsonValue(name.martingeisse.common.javascript.ownjson.ast.JsonAstValue)
	 */
	@Override
	protected void handleJsonValue(final JsonAstValue value) {
		properties.add(new JsonAstObjectProperty(propertyName, value));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.parser.AbstractJsonParserAstBuilderState#handleEndObject(int, int, int, int)
	 */
	@Override
	protected AbstractJsonParserState handleEndObject(int startLine, int startColumn, int endLine, int endColumn) {
		JsonAstObjectProperty[] propertyArray = properties.toArray(new JsonAstObjectProperty[properties.size()]);
		getParentState().handleJsonValue(new JsonAstObject(getStartLine(), getStartColumn(), endLine, endColumn, propertyArray));
		return getParentState();
	}
	
}
