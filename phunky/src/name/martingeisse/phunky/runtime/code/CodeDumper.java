/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code;


/**
 * This class helps dump the PHP code itself.
 */
public final class CodeDumper {

	/**
	 * the builder
	 */
	private final StringBuilder builder;
	
	/**
	 * the indentationLevel
	 */
	private int indentationLevel;
	
	/**
	 * the startOfLine
	 */
	private boolean startOfLine;

	/**
	 * Constructor.
	 */
	public CodeDumper() {
		this.builder = new StringBuilder();
		this.indentationLevel = 0;
		this.startOfLine = true;
	}

	/**
	 * Constructor.
	 * @param globalIndentationLevel a global indentation level to apply to all lines
	 */
	public CodeDumper(int globalIndentationLevel) {
		this.builder = new StringBuilder();
		this.indentationLevel = globalIndentationLevel;
		this.startOfLine = true;
	}
	
	/**
	 * Getter method for the builder.
	 * @return the builder
	 */
	public StringBuilder getBuilder() {
		return builder;
	}
	
	/**
	 * Getter method for the indentationLevel.
	 * @return the indentationLevel
	 */
	public int getIndentationLevel() {
		return indentationLevel;
	}
	
	/**
	 * Increases the indentation level by 1.
	 */
	public void increaseIndentation() {
		indentationLevel++;
	}
	
	/**
	 * Decreases the indentation level by 1.
	 */
	public void decreaseIndentation() {
		indentationLevel--;
	}
	
	/**
	 * Prints a single character of code.
	 * 
	 * Note that the argument should not be a newline character itself
	 * because indentation would not be applied to it.
	 * 
	 * @param c the character to print
	 */
	public void print(char c) {
		handleStartOfLine();
		builder.append(c);
	}
	
	/**
	 * Prints a piece of code.
	 * 
	 * Note that the argument should not contain newline characters itself
	 * because indentation would not be applied to them.
	 * 
	 * @param s the string to print
	 */
	public void print(String s) {
		handleStartOfLine();
		builder.append(s);
	}
	
	/**
	 * Prints a piece of code followed by a newline character.
	 * 
	 * Note that the argument should not contain newline characters itself
	 * because indentation would not be applied to them.
	 * 
	 * @param s the string to print
	 */
	public void println(String s) {
		handleStartOfLine();
		builder.append(s);
		builder.append('\n');
	}
	
	/**
	 * If the most recently added character is a newline character, then this
	 * method removes it again. This is used when language constructs such
	 * as statements, which normally end a line, are used inline (like in
	 * a for statement).
	 */
	public void removeRecentNewline() {
		if (builder.charAt(builder.length() - 1) == '\n') {
			builder.setLength(builder.length() - 1);
		}
	}
	
	/**
	 * 
	 */
	private void handleStartOfLine() {
		if (startOfLine) {
			for (int i=0; i<indentationLevel; i++) {
				builder.append('\t');
			}
			startOfLine = false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return builder.toString();
	}
	
}
