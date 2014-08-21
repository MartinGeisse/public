/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;


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
	 * @param name the property name
	 * @param value the property value
	 */
	public JsonAstObjectProperty(JsonAstString name, JsonAstNode value) {
		super(name.getLine(), name.getColumn());
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
