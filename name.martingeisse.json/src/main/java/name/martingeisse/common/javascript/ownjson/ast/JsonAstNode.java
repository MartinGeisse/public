/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

/**
 * Base class for JSON AST nodes.
 */
public abstract class JsonAstNode {

	/**
	 * the startLine
	 */
	private final int startLine;

	/**
	 * the startLine
	 */
	private final int startColumn;

	/**
	 * the endLine
	 */
	private final int endLine;

	/**
	 * the endLine
	 */
	private final int endColumn;

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 */
	public JsonAstNode(final int startLine, final int startColumn, final int endLine, final int endColumn) {
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
	}

	/**
	 * Getter method for the startLine.
	 * @return the startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Getter method for the startColumn.
	 * @return the startColumn
	 */
	public int getStartColumn() {
		return startColumn;
	}

	/**
	 * Getter method for the endLine.
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * Getter method for the endColumn.
	 * @return the endColumn
	 */
	public int getEndColumn() {
		return endColumn;
	}

}
