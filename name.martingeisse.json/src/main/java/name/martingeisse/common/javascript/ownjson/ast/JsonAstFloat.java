/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

/**
 * A node that contains a (double-precision) floating-point value.
 */
public final class JsonAstFloat extends JsonAstValue {

	/**
	 * the value
	 */
	private final double value;

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param value the value of this node
	 */
	public JsonAstFloat(final int startLine, final int startColumn, final int endLine, final int endColumn, double value) {
		super(startLine, startColumn, endLine, endColumn);
		this.value = value;
	}
	
	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 * @param value the value of this node
	 */
	public JsonAstFloat(final JsonAstNode location, double value) {
		super(location);
		this.value = value;
	}
	
	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.ast.JsonAstValue#withLocation(name.martingeisse.common.javascript.ownjson.ast.JsonAstNode)
	 */
	@Override
	public JsonAstFloat withLocation(JsonAstNode location) {
		return new JsonAstFloat(location, value);
	}

}
