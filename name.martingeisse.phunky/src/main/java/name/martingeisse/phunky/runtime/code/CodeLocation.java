/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;

/**
 * A location in the source code, as used by the AST.
 * 
 * Line and column numbers used by this class are 0-based.
 */
public final class CodeLocation {

	/**
	 * the filePath
	 */
	private final String filePath;

	/**
	 * the line
	 */
	private final int line;

	/**
	 * the column
	 */
	private final int column;

	/**
	 * Constructor.
	 * @param filePath the path to the source code file
	 * @param line the line number
	 * @param column the column number
	 */
	public CodeLocation(final String filePath, final int line, final int column) {
		this.filePath = filePath;
		this.line = line;
		this.column = column;
	}

	/**
	 * Getter method for the filePath.
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "file " + filePath + ", line " + (line + 1) + ", column " + (column + 1);
	}
	
}
