/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.parserbase;

/**
 * Base class for the application-specific parser state implementation.
 * 
 * Each method should return the next parser state to use.
 */
public abstract class AbstractJsonParserState {

	/**
	 * Callback method to handle a JSON null value.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleNullValue(int line, int column);

	/**
	 * Callback method to handle a JSON boolean value.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleBooleanValue(int line, int column, boolean value);
	
	/**
	 * Callback method to handle a JSON integer value.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleIntegerValue(int line, int column, long value);
	
	/**
	 * Callback method to handle a JSON floating-point value.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleFloatingPointValue(int line, int column, double value);
	
	/**
	 * Callback method to handle a JSON string value.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleStringValue(int line, int column, String value);
	
	/**
	 * Callback method to handle the start of a JSON array.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleBeginArray(int line, int column);
	
	/**
	 * Callback method to handle the end of a JSON array.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleEndArray(int line, int column);
	
	/**
	 * Callback method to handle the start of a JSON object.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleBeginObject(int line, int column);

	/**
	 * Callback method to handle the name of a JSON object property.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @param name the property name
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleObjectPropertyName(int line, int column, String name);

	/**
	 * Callback method to handle the end of a JSON object.
	 * 
	 * @param line the line in the source code
	 * @param column the column in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleEndObject(int line, int column);
	
}
