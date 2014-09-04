/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

import name.martingeisse.common.util.ParameterUtil;

/**
 * A node that contains a string value.
 */
public final class JsonAstString extends JsonAstValue {

	/**
	 * the value
	 */
	private final String value;

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param value the value of this node
	 */
	public JsonAstString(final int startLine, final int startColumn, final int endLine, final int endColumn, String value) {
		super(startLine, startColumn, endLine, endColumn);
		this.value = ParameterUtil.ensureNotNull(value, "value");
	}

	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 * @param value the value of this node
	 */
	public JsonAstString(final JsonAstNode location, String value) {
		super(location);
		this.value = ParameterUtil.ensureNotNull(value, "value");
	}
	
	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.ast.JsonAstValue#withLocation(name.martingeisse.common.javascript.ownjson.ast.JsonAstNode)
	 */
	@Override
	public JsonAstString withLocation(JsonAstNode location) {
		return new JsonAstString(location, value);
	}

}
