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
		this.properties = buildProperties(properties);
	}
	
	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 * @param properties the properties of this node
	 */
	public JsonAstObject(final JsonAstNode location, JsonAstObjectProperty[] properties) {
		super(location);
		this.properties = buildProperties(properties);
	}
	
	/**
	 * Constructor.
	 * @param location a node that specifies the location of this node
	 * @param properties the properties of this node
	 */
	private JsonAstObject(final JsonAstNode location, Map<String, JsonAstObjectProperty> properties) {
		super(location);
		this.properties = properties;
	}
	
	/**
	 * 
	 */
	private static Map<String, JsonAstObjectProperty> buildProperties(JsonAstObjectProperty[] properties) {
		Map<String, JsonAstObjectProperty> result = new HashMap<>();
		for (JsonAstObjectProperty property : properties) {
			result.put(property.getName().getValue(), property);
		}
		return result;
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

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.ownjson.ast.JsonAstValue#withLocation(name.martingeisse.common.javascript.ownjson.ast.JsonAstNode)
	 */
	@Override
	public JsonAstValue withLocation(JsonAstNode location) {
		Map<String, JsonAstObjectProperty> copyOfProperties = new HashMap<>();
		for (Map.Entry<String, JsonAstObjectProperty> entry : properties.entrySet()) {
			copyOfProperties.put(entry.getKey(), entry.getValue().withLocation(location));
		}
		return new JsonAstObject(location, copyOfProperties);
	}
	
}
