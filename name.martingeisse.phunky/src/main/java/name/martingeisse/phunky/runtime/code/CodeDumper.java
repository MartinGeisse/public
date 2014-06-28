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
	 * the indentationSegment
	 */
	private String indentationSegment;

	/**
	 * Constructor.
	 */
	public CodeDumper() {
		this(0);
	}

	/**
	 * Constructor.
	 * @param globalIndentationLevel a global indentation level to apply to all lines
	 */
	public CodeDumper(int globalIndentationLevel) {
		this.builder = new StringBuilder();
		this.indentationLevel = globalIndentationLevel;
		this.startOfLine = true;
		this.indentationSegment = "    ";
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
	 * Getter method for the indentationSegment.
	 * @return the indentationSegment
	 */
	public String getIndentationSegment() {
		return indentationSegment;
	}
	
	/**
	 * Setter method for the indentationSegment.
	 * @param indentationSegment the indentationSegment to set
	 */
	public void setIndentationSegment(String indentationSegment) {
		this.indentationSegment = indentationSegment;
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
		if (c == '\n') {
			builder.append('\n');
			startOfLine = true;
		} else {
			handleStartOfLine();
			builder.append(c);
		}
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
		int position = 0;
		while (true) {
			int index = s.indexOf('\n', position);
			if (index == -1) {
				break;
			}
			if (index > position) {
				handleStartOfLine();
				builder.append(s.substring(position, index));
			}
			builder.append('\n');
			position = index + 1;
		}
		if (position < s.length()) {
			handleStartOfLine();
			builder.append(s.substring(position));
		}
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
		print(s);
		builder.append('\n');
		startOfLine = true;
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
				builder.append(indentationSegment);
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
