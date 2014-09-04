/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

/**
 * A node that contains the null value.
 */
public final class JsonAstNull extends JsonAstValue {

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 */
	public JsonAstNull(final int startLine, final int startColumn, final int endLine, final int endColumn) {
		super(startLine, startColumn, endLine, endColumn);
	}

	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 */
	public JsonAstNull(final JsonAstNode location) {
		super(location);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.ast.JsonAstValue#withLocation(name.martingeisse.common.javascript.ownjson.ast.JsonAstNode)
	 */
	@Override
	public JsonAstNull withLocation(JsonAstNode location) {
		return new JsonAstNull(location);
	}

}
