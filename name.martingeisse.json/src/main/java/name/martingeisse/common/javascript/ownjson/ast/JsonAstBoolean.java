/**
 * Copyright (c) 2013 Shopgate GmbH
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
	 * @param line the line number
	 * @param column the column number
	 * @param value the value of this node
	 */
	public JsonAstBoolean(int line, int column, boolean value) {
		super(line, column);
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public boolean isValue() {
		return value;
	}
	
}
