/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * Used to signal a syntax error
 */
public final class JsonSyntaxException extends RuntimeException {

	/**
	 * the line
	 */
	private final int line;

	/**
	 * the column
	 */
	private final int column;
	
	/**
	 * the rawMessage
	 */
	private final String rawMessage;

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 * @param expected the expected input
	 * @param actual the actual input
	 */
	public JsonSyntaxException(int line, int column, String expected, String actual) {
		this(line, column, "expected " + expected + ", found " + actual);
	}

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 * @param rawMessage the "raw" message, without line/column information
	 */
	public JsonSyntaxException(int line, int column, String rawMessage) {
		super("syntax error in line " + (line + 1) + ", column " + (column + 1) + ": " + rawMessage);
		this.line = line;
		this.column = column;
		this.rawMessage = rawMessage;
	}
	
	/**
	 * Getter method for the line.
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Getter method for the column.
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Getter method for the rawMessage.
	 * @return the rawMessage
	 */
	public String getRawMessage() {
		return rawMessage;
	}
	
}
