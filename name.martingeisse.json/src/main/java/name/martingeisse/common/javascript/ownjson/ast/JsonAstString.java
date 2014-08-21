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
	 * @param line the line number
	 * @param column the column number
	 * @param value the value of this node
	 */
	public JsonAstString(int line, int column, String value) {
		super(line, column);
		this.value = ParameterUtil.ensureNotNull(value, "value");
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
}
