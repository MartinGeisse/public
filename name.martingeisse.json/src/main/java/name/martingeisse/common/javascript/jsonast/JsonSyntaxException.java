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
	 * the expected
	 */
	private final String expected;

	/**
	 * the actual
	 */
	private final String actual;

	/**
	 * Constructor.
	 * @param line the line number
	 * @param column the column number
	 * @param expected the expected input
	 * @param actual the actual input
	 */
	public JsonSyntaxException(int line, int column, String expected, String actual) {
		super("syntax error in line " + (line + 1) + ", column " + (column + 1) + ": expected " + expected + ", found " + actual);
		this.line = line;
		this.column = column;
		this.expected = expected;
		this.actual = actual;
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
	 * Getter method for the expected.
	 * @return the expected
	 */
	public String getExpected() {
		return expected;
	}

	/**
	 * Getter method for the actual.
	 * @return the actual
	 */
	public String getActual() {
		return actual;
	}

}
