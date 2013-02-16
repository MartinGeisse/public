/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.wave;

/**
 * Thrown on VCD syntax errors.
 */
public class SyntaxException extends Exception {

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
	 * @param line the line where the syntax error occurred
	 * @param column the column where the syntax error occurred
	 * @param rawMessage the actual error message
	 */
	public SyntaxException(int line, int column, String rawMessage) {
		super("syntax error at " + line + ", " + column + ": " + rawMessage);
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
