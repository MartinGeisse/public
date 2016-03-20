/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.ast;

/**
 * A node that contains a boolean value.
 */
public final class JsonAstBoolean extends JsonAstValue {

	/**
	 * the value
	 */
	private final boolean value;

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param value the value of this node
	 */
	public JsonAstBoolean(final int startLine, final int startColumn, final int endLine, final int endColumn, boolean value) {
		super(startLine, startColumn, endLine, endColumn);
		this.value = value;
	}
	
	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 * @param value the value of this node
	 */
	public JsonAstBoolean(final JsonAstNode location, boolean value) {
		super(location);
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public boolean isValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.ast.JsonAstValue#withLocation(name.martingeisse.common.javascript.ownjson.ast.JsonAstNode)
	 */
	@Override
	public JsonAstBoolean withLocation(JsonAstNode location) {
		return new JsonAstBoolean(location, value);
	}

}
