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
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param value the value of this node
	 */
	public JsonAstInteger(final int startLine, final int startColumn, final int endLine, final int endColumn, long value) {
		super(startLine, startColumn, endLine, endColumn);
		this.value = value;
	}
	
	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 * @param value the value of this node
	 */
	public JsonAstInteger(final JsonAstNode location, long value) {
		super(location);
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
