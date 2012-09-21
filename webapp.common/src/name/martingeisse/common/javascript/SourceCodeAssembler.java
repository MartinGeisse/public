/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript;

import java.io.IOException;
import java.io.Writer;

/**
 * This class implements common functionality for source
 * code assembling, mainly keeping a string builder and
 * handling indentation.
 */
public class SourceCodeAssembler {

	/**
	 * the builder
	 */
	private final StringBuilder builder;

	/**
	 * the current identation level
	 */
	private int currentIdentationLevel;

	/**
	 * Constructor.
	 */
	public SourceCodeAssembler() {
		this.builder = new StringBuilder();
		this.currentIdentationLevel = 0;
	}

	/**
	 * Getter method for the currentIdentationLevel.
	 * @return the currentIdentationLevel
	 */
	public int getCurrentIdentationLevel() {
		return currentIdentationLevel;
	}

	/**
	 * Setter method for the currentIdentationLevel.
	 * @param currentIdentationLevel the currentIdentationLevel to set
	 */
	public void setCurrentIdentationLevel(final int currentIdentationLevel) {
		if (currentIdentationLevel < 0) {
			throw new IllegalArgumentException("the argument is negative: " + currentIdentationLevel);
		}
		this.currentIdentationLevel = currentIdentationLevel;
	}

	/**
	 * Getter method for the builder.
	 * @return the builder
	 */
	public StringBuilder getBuilder() {
		return builder;
	}

	/**
	 * Increments the identation level by 1.
	 */
	public void incrementIndentation() {
		currentIdentationLevel++;
	}

	/**
	 * Decrements the identation level by 1.
	 * @throws IllegalStateException if the indentation level is already zero.
	 */
	public void decrementIdentation() {
		if (currentIdentationLevel == 0) {
			throw new IllegalStateException("identation level is already zero");
		}
		currentIdentationLevel--;
	}

	/**
	 * @return the assembled source code. This method just returns
	 * getBuilder().toString().
	 */
	public String getAssembledCode() {
		return getBuilder().toString();
	}
	
	/**
	 * Resets this assembler to its initial state.
	 */
	public void reset() {
		builder.setLength(0);
		currentIdentationLevel = 0;
	}
	
	/**
	 * Flushes the contents of this assembler to the specified
	 * writer. This sets the current contents of the assembler to
	 * the empty string but retains the indentation level.
	 * @param w the writer
	 */
	public void flushTo(Writer w) throws IOException {
		w.write(builder.toString());
		builder.setLength(0);
	}

	/**
	 * Appends the character c (count) times to the builder.
	 * @param c the character to repeat
	 * @param count the number of times the character shall be repeated
	 */
	public void appendRepeated(final char c, final int count) {
		for (int i = 0; i < count; i++) {
			builder.append(c);
		}
	}

	/**
	 * Appends the string s (count) times to the builder.
	 * @param s the string to repeat
	 * @param count the number of times the string shall be repeated
	 */
	public void appendRepeated(final String s, final int count) {
		if (s == null) {
			throw new IllegalArgumentException("argument string is null");
		}
		for (int i = 0; i < count; i++) {
			builder.append(s);
		}
	}

	/**
	 * Prints an indentation string corresponding to the current
	 * indentation level. The default implementation 
	 */
	public void indent() {
		appendRepeated('\t', getCurrentIdentationLevel());
	}

	/**
	 * Appends a line including indentation, the specified content, and
	 * a newline character to the builder.
	 * @param content the line content
	 */
	public void appendIndentedLine(final String content) {
		if (content == null) {
			throw new IllegalArgumentException("content argument is null");
		}
		indent();
		getBuilder().append(content).append('\n');
	}

}
