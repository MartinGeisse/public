/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.ast;

import java.util.HashMap;
import java.util.Map;

/**
 * A node that contains an object.
 */
public final class JsonAstObject extends JsonAstValue {

	/**
	 * the properties
	 */
	private final Map<String, JsonAstObjectProperty> properties;

	/**
	 * Constructor.
	 * @param startLine the starting line of the node
	 * @param startColumn the starting column of the node
	 * @param endLine the ending line of the node
	 * @param endColumn the ending column of the node
	 * @param properties the properties of this node
	 */
	public JsonAstObject(final int startLine, final int startColumn, final int endLine, final int endColumn, JsonAstObjectProperty[] properties) {
		super(startLine, startColumn, endLine, endColumn);
		this.properties = new HashMap<>();
		for (JsonAstObjectProperty property : properties) {
			this.properties.put(property.getName().getValue(), property);
		}
	}

	/**
	 * Getter method for the elementCount.
	 * @return the elementCount
	 */
	public int getElementCount() {
		return properties.size();
	}
	
	/**
	 * Getter method for a property.
	 * @param propertyName the property name
	 * @return the property
	 */
	public JsonAstObjectProperty getProperty(String propertyName) {
		return properties.get(propertyName);
	}
	
	/**
	 * Getter method for a property value.
	 * @param propertyName the property name
	 * @return the property value
	 */
	public JsonAstNode getValue(String propertyName) {
		return properties.get(propertyName).getValue();
	}
	
}
