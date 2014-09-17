/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.synthesis.codegen;

import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;

/**
 * This is the base class for code assemblers. It handles basic
 * mechanisms such as indentation.
 * 
 * Empty lines are indented by this class.
 */
public class CodeAssembler {

	/**
	 * the printWriter
	 */
	private final PrintWriter printWriter;
	
	/**
	 * the indentation
	 */
	private int indentation;
	
	/**
	 * the atStartOfLine
	 */
	private boolean atStartOfLine;
	
	/**
	 * Constructor
	 * @param printWriter the print writer used to write code
	 */
	public CodeAssembler(PrintWriter printWriter) {
		this.printWriter = printWriter;
		this.indentation = 0;
		this.atStartOfLine = true;
	}

	/**
	 * @return Returns the printWriter.
	 */
	public final PrintWriter getPrintWriter() {
		return printWriter;
	}

	/**
	 * @return Returns the indentation.
	 */
	public final int getIndentation() {
		return indentation;
	}

	/**
	 * Sets the indentation.
	 * @param indentation the new value to set
	 */
	public final void setIndentation(int indentation) {
		if (indentation < 0) {
			throw new IllegalArgumentException("trying to set negative indentation: " + indentation);
		}
		this.indentation = indentation;
	}

	/**
	 * Increases the indentation level by 1.
	 */
	public final void increaseIndentation() {
		indentation++;
	}

	/**
	 * Decreases the indentation level by 1.
	 */
	public final void decreaseIndentation() {
		if (indentation == 0) {
			throw new IllegalStateException("indentation is already 0");
		}
		indentation--;
	}
	
	/**
	 * Prints indentation to begin a line. This method simply
	 * prints tab characters a number of times equal to the
	 * indentation level.
	 */
	private final void printIndentation() {
		for (int i=0; i<indentation; i++) {
			printWriter.append('\t');
		}
	}
	
	/**
	 * Prints a string that is expected to contain no newline characters.
	 * @param s the segment
	 */
	private final void printSegment(String s) {
		if (atStartOfLine) {
			printIndentation();
			atStartOfLine = false;
		}
		printWriter.append(s);
	}
	
	/**
	 * Prints a newline.
	 */
	public final void println() {
		if (atStartOfLine) {
			printIndentation();
		}
		printWriter.println();
		atStartOfLine = true;
	}
	
	/**
	 * Prints the specified string.
	 * @param s the string to print
	 */
	public final void print(String s) {
		boolean first = true;
		for (String segment : StringUtils.split(s, '\n')) {
			if (first) {
				first = false;
			} else {
				println();
			}
			printSegment(segment);
		}
	}

	/**
	 * Prints the specified string followed by a newline.
	 * @param s the string to print
	 */
	public final void println(String s) {
		print(s);
		println();
	}

}
