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
	 * Getter method for the value.
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	
}
