/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

/**
 * Base class for JSON AST value nodes.
 */
public abstract class JsonAstValue extends JsonAstNode {

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 */
	public JsonAstValue(final int startLine, final int startColumn, final int endLine, final int endColumn) {
		super(startLine, startColumn, endLine, endColumn);
	}

}
