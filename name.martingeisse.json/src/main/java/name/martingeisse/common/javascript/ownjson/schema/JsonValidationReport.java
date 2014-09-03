/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.schema;

import java.util.ArrayList;
import java.util.List;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstNode;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstObject;
import name.martingeisse.common.javascript.ownjson.ast.JsonAstValue;

/**
 * Stores validation messages while validating a JSON struture
 * against a schema.
 */
public final class JsonValidationReport {

	/**
	 * the MISSING_PROPERTY_MESSAGE_PREFIX
	 */
	private static final String MISSING_PROPERTY_MESSAGE_PREFIX = "missing object property: ";
	
	/**
	 * the WRONG_TYPE_MESSAGE_PREFIX
	 */
	private static final String WRONG_TYPE_MESSAGE_PREFIX = "wrong type, expected ";

	/**
	 * the TYPE_COERCION_MESSAGE_PREFIX
	 */
	private static final String TYPE_COERCION_MESSAGE_PREFIX = "wrong type, coerced to ";

	/**
	 * the messages
	 */
	private final List<JsonValidationMessage> messages = new ArrayList<>();
	
	/**
	 * Constructor.
	 */
	public JsonValidationReport() {
	}
	
	/**
	 * Adds a message to this report.
	 * @param message the message to add
	 */
	public void addMessage(JsonValidationMessage message) {
		messages.add(message);
	}
	
	/**
	 * Adds a warning (non-fatal) message.
	 * @param source the source node
	 * @param message the message text
	 */
	public void addWarning(JsonAstNode source, String message) {
		messages.add(new JsonValidationMessage(source, message, false));
	}
	
	/**
	 * Adds an error (fatal) message.
	 * @param source the source node
	 * @param message the message text
	 */
	public void addError(JsonAstNode source, String message) {
		messages.add(new JsonValidationMessage(source, message, true));
	}
	
	/**
	 * Adds a warning message about a missing object property.
	 * @param parentObject the parent object from which the property is missing
	 * @param propertyName the name of the missing property
	 */
	public void addMissingPropertyWarning(JsonAstNode parentObject, String propertyName) {
		messages.add(new JsonValidationMessage(parentObject, MISSING_PROPERTY_MESSAGE_PREFIX + propertyName, false));
	}
	
	/**
	 * Adds an error message about a missing object property.
	 * @param parentObject the parent object from which the property is missing
	 * @param propertyName the name of the missing property
	 */
	public void addMissingPropertyError(JsonAstObject parentObject, String propertyName) {
		messages.add(new JsonValidationMessage(parentObject, MISSING_PROPERTY_MESSAGE_PREFIX + propertyName, true));
	}
	
	/**
	 * Adds a warning message about a value having the wrong type and being coerced
	 * to the correct type.
	 * @param value the value whose type is wrong
	 * @param expectedType a description of the expected type
	 */
	public void addTypeCoercionWarning(JsonAstValue value, String expectedType) {
		messages.add(new JsonValidationMessage(value, TYPE_COERCION_MESSAGE_PREFIX + expectedType, false));
	}
	
	/**
	 * Adds an error message about a value having the wrong type.
	 * @param value the value whose type is wrong
	 * @param expectedType a description of the expected type
	 */
	public void addWrongTypeError(JsonAstValue value, String expectedType) {
		messages.add(new JsonValidationMessage(value, WRONG_TYPE_MESSAGE_PREFIX + expectedType, true));
	}
	
	/**
	 * Getter method for the validation messages.
	 * @return the messages
	 */
	public Iterable<JsonValidationMessage> getMessages() {
		return messages;
	}
	
}

