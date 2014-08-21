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
	 * @param line the line number
	 * @param column the column number
	 * @param elements the elements of this node
	 */
	public JsonAstArray(int line, int column, JsonAstNode[] elements) {
		this(line, column, elements, true);
	}

	/**
	 * Internal Constructor that controls copying behavior.
	 * @param line the line number
	 * @param column the column number
	 * @param elements the elements of this node
	 * @param copyElementsArray whether the elements array should be copied
	 */
	JsonAstArray(int line, int column, JsonAstNode[] elements, boolean copyElementsArray) {
		super(line, column);
		this.elements = (copyElementsArray ? Arrays.copyOf(elements, elements.length) : elements);
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
