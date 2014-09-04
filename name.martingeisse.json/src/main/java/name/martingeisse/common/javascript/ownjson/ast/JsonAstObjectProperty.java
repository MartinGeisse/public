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
	private final JsonAstValue value;

	/**
	 * Constructor.
	 * @param name the property name
	 * @param value the property value
	 */
	public JsonAstObjectProperty(JsonAstString name, JsonAstValue value) {
		super(name.getStartLine(), name.getStartColumn(), value.getEndLine(), value.getEndColumn());
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
	public JsonAstValue getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.ast.JsonAstNode#withLocation(name.martingeisse.common.javascript.ownjson.ast.JsonAstNode)
	 */
	@Override
	public JsonAstObjectProperty withLocation(JsonAstNode location) {
		return new JsonAstObjectProperty(name.withLocation(location), value.withLocation(location));
	}
	
}
