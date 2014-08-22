/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.parserbase;

/**
 * Used to signal a syntax error
 */
public final class JsonSyntaxException extends RuntimeException {

	/**
	 * the startLine
	 */
	private final int startLine;

	/**
	 * the startColumn
	 */
	private final int startColumn;

	/**
	 * the endLine
	 */
	private final int endLine;

	/**
	 * the endColumn
	 */
	private final int endColumn;

	/**
	 * the rawMessage
	 */
	private final String rawMessage;

	/**
	 * Constructor.
	 * @param startLine the start line number
	 * @param startColumn the start column number
	 * @param endLine the end line number
	 * @param endColumn the end column number
	 * @param expected the expected input
	 * @param actual the actual input
	 */
	public JsonSyntaxException(final int startLine, final int startColumn, final int endLine, final int endColumn, final String expected, final String actual) {
		this(startLine, startColumn, endLine, endColumn, "expected " + expected + ", found " + actual);
	}

	/**
	 * Constructor.
	 * @param startLine the start line number
	 * @param startColumn the start column number
	 * @param endLine the end line number
	 * @param endColumn the end column number
	 * @param rawMessage the "raw" message, without line/column information
	 */
	public JsonSyntaxException(final int startLine, final int startColumn, final int endLine, final int endColumn, final String rawMessage) {
		super("syntax error in line " + (startLine + 1) + ", column " + (startColumn + 1) + ": " + rawMessage);
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
		this.rawMessage = rawMessage;
	}

	/**
	 * Getter method for the startLine.
	 * @return the startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * Getter method for the startColumn.
	 * @return the startColumn
	 */
	public int getStartColumn() {
		return startColumn;
	}

	/**
	 * Getter method for the endLine.
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * Getter method for the endColumn.
	 * @return the endColumn
	 */
	public int getEndColumn() {
		return endColumn;
	}

	/**
	 * Getter method for the rawMessage.
	 * @return the rawMessage
	 */
	public String getRawMessage() {
		return rawMessage;
	}

}
