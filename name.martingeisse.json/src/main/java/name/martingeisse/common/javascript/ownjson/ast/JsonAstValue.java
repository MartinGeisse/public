/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
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

	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 */
	public JsonAstValue(final JsonAstNode location) {
		super(location);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.ast.JsonAstNode#withLocation(name.martingeisse.common.javascript.ownjson.ast.JsonAstNode)
	 */
	@Override
	public abstract JsonAstValue withLocation(JsonAstNode location);

}
