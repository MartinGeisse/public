/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;

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
	 * @param line the line number
	 * @param column the column number
	 * @param value the value of this node
	 */
	public JsonAstFloat(int line, int column, double value) {
		super(line, column);
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
