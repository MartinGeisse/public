/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;


/**
 * A node that contains an object property.
 */
public final class JsonAstObjectProperty extends JsonAstNode {

	/**
	 * the name
	 */
	private final JsonAstString name;

	/**
	 * the value
	 */
	private final JsonAstNode value;

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 * @param name the property name
	 * @param value the property value
	 */
	public JsonAstObjectProperty(int line, int column, JsonAstString name, JsonAstNode value) {
		super(line, column);
		this.name = name;
		this.value = value;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public JsonAstString getName() {
		return name;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public JsonAstNode getValue() {
		return value;
	}

}
