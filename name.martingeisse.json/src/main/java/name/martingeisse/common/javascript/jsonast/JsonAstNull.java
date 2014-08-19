/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * A node that contains the null value.
 */
public final class JsonAstNull extends JsonAstValue {

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 */
	public JsonAstNull(int line, int column) {
		super(line, column);
	}

}
