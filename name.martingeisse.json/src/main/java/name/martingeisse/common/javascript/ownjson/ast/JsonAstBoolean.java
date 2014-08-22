/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

/**
 * A node that contains a boolean value.
 */
public final class JsonAstBoolean extends JsonAstValue {

	/**
	 * the value
	 */
	private final boolean value;

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param value the value of this node
	 */
	public JsonAstBoolean(final int startLine, final int startColumn, final int endLine, final int endColumn, boolean value) {
		super(startLine, startColumn, endLine, endColumn);
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public boolean isValue() {
		return value;
	}
	
}
