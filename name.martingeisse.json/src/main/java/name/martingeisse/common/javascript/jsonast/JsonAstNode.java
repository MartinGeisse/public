/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * Base class for JSON AST nodes.
 */
public abstract class JsonAstNode {

	/**
	 * the line
	 */
	private final int line;

	/**
	 * the column
	 */
	private final int column;

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 */
	public JsonAstNode(int line, int column) {
		this.line = line;
		this.column = column;
	}

	/**
	 * Getter method for the line.
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Getter method for the column.
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
}
