/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

/**
 * A node that contains a (long) integer value.
 */
public final class JsonAstInteger extends JsonAstValue {

	/**
	 * the value
	 */
	private final long value;

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 * @param value the value of this node
	 */
	public JsonAstInteger(int line, int column, long value) {
		super(line, column);
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public long getValue() {
		return value;
	}
	
}
