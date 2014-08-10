/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror.compile;

import java.io.Serializable;

/**
 * A marker that tells something about the code and is generated
 * by a compiler.
 */
public final class CompilerMarker implements Serializable {

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
	 * the errorLevel
	 */
	private final CompilerErrorLevel errorLevel;

	/**
	 * the message
	 */
	private final String message;

	/**
	 * Constructor.
	 * @param startLine the starting line
	 * @param startColumn the starting column
	 * @param endLine the ending line
	 * @param endColumn the ending column
	 * @param errorLevel the error level
	 * @param message the message for the user
	 */
	public CompilerMarker(final int startLine, final int startColumn, final int endLine, final int endColumn, final CompilerErrorLevel errorLevel, final String message) {
		super();
		this.startLine = startLine;
		this.startColumn = startColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
		this.errorLevel = errorLevel;
		this.message = message;
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
	 * Getter method for the errorLevel.
	 * @return the errorLevel
	 */
	public CompilerErrorLevel getErrorLevel() {
		return errorLevel;
	}

	/**
	 * Getter method for the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
