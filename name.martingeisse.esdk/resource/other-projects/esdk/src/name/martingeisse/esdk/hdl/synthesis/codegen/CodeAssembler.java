/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.synthesis.codegen;

import java.io.PrintWriter;

/**
 * This is the base class for code assemblers. It handles basic
 * mechanisms such as indentation.
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
	 * Constructor
	 * @param printWriter the print writer used to write code
	 */
	public CodeAssembler(PrintWriter printWriter) {
		this.printWriter = printWriter;
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
	 * @return Returns the printWriter.
	 */
	public final PrintWriter getPrintWriter() {
		return printWriter;
	}

	/**
	 * Increases the indentation level by 1.
	 */
	public final void startIndentation() {
		indentation++;
	}

	/**
	 * Decreases the indentation level by 1.
	 */
	public final void endIndentation() {
		if (indentation == 0) {
			throw new IllegalStateException("indentation is 0");
		}
		indentation--;
	}
	
	/**
	 * Prints indentation to begin a line. This method simply
	 * prints tab characters a number of times equal to the
	 * indentation level.
	 */
	public final void printIndentation() {
		for (int i=0; i<indentation; i++) {
			printWriter.append('\t');
		}
	}
	
	/**
	 * Prints a newline.
	 */
	public final void println() {
		printWriter.println();
	}
	
	/**
	 * Prints the specified string.
	 * @param s the string to print
	 */
	public final void print(String s) {
		printWriter.print(s);
	}

	/**
	 * Prints the specified string followed by a newline.
	 * @param s the string to print
	 */
	public final void println(String s) {
		printWriter.println(s);
	}

	/**
	 * Prints indentation, then the specified string followed by a newline.
	 * @param s the string to print
	 */
	public final void printWholeLine(String s) {
		printIndentation();
		printWriter.println(s);
	}

}
