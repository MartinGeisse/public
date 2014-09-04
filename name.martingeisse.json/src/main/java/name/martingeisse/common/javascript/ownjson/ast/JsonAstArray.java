/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

import java.util.Arrays;

/**
 * A node that contains an array.
 */
public final class JsonAstArray extends JsonAstValue {

	/**
	 * the elements
	 */
	private final JsonAstNode[] elements;

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param elements the elements of this node
	 */
	public JsonAstArray(final int startLine, final int startColumn, final int endLine, final int endColumn, JsonAstNode[] elements) {
		super(startLine, startColumn, endLine, endColumn);
		this.elements = buildElements(elements, true);
	}

	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 * @param elements the elements of this node
	 */
	public JsonAstArray(final JsonAstNode location, JsonAstNode[] elements) {
		super(location);
		this.elements = buildElements(elements, true);
	}

	/**
	 * Internal Constructor that controls copying behavior.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param elements the elements of this node
	 * @param copyElementsArray whether the elements array should be copied
	 */
	JsonAstArray(final int startLine, final int startColumn, final int endLine, final int endColumn, JsonAstNode[] elements, boolean copyElementsArray) {
		super(startLine, startColumn, endLine, endColumn);
		this.elements = buildElements(elements, copyElementsArray);
	}
	
	/**
	 * 
	 */
	private static JsonAstNode[] buildElements(JsonAstNode[] elements, boolean copyElementsArray) {
		return (copyElementsArray ? Arrays.copyOf(elements, elements.length) : elements);
	}

	/**
	 * Getter method for the elementCount.
	 * @return the elementCount
	 */
	public int getElementCount() {
		return elements.length;
	}
	
	/**
	 * Getter method for an element.
	 * @param index the element index
	 * @return the element
	 */
	public JsonAstNode getElement(int index) {
		return elements[index];
	}
	
}