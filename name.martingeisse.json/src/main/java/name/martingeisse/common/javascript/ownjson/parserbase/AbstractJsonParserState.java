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
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleNullValue(int startLine, int startColumn, int endLine, int endColumn);

	/**
	 * Callback method to handle a JSON boolean value.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleBooleanValue(int startLine, int startColumn, int endLine, int endColumn, boolean value);
	
	/**
	 * Callback method to handle a JSON integer value.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleIntegerValue(int startLine, int startColumn, int endLine, int endColumn, long value);
	
	/**
	 * Callback method to handle a JSON floating-point value.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleFloatingPointValue(int startLine, int startColumn, int endLine, int endColumn, double value);
	
	/**
	 * Callback method to handle a JSON string value.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @param value the value to handle
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleStringValue(int startLine, int startColumn, int endLine, int endColumn, String value);
	
	/**
	 * Callback method to handle the start of a JSON array.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleBeginArray(int startLine, int startColumn, int endLine, int endColumn);
	
	/**
	 * Callback method to handle the end of a JSON array.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleEndArray(int startLine, int startColumn, int endLine, int endColumn);
	
	/**
	 * Callback method to handle the start of a JSON object.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleBeginObject(int startLine, int startColumn, int endLine, int endColumn);

	/**
	 * Callback method to handle the name of a JSON object property.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @param name the property name
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleObjectPropertyName(int startLine, int startColumn, int endLine, int endColumn, String name);

	/**
	 * Callback method to handle the end of a JSON object.
	 * 
	 * @param startLine the starting line in the source code
	 * @param startColumn the starting column in the source code
	 * @param endLine the ending line in the source code
	 * @param endColumn the endingcolumn in the source code
	 * @return the next state to use
	 */
	protected abstract AbstractJsonParserState handleEndObject(int startLine, int startColumn, int endLine, int endColumn);
	
}
