/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * Base class for JSON AST value nodes.
 */
public abstract class JsonAstValue extends JsonAstNode {

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 */
	public JsonAstValue(int line, int column) {
		super(line, column);
	}

}
