/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.assembler.parser;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;

/**
 * Indicates a syntax error whose immediate effect is constrained
 * to the line in which is occurred.
 */
public class LineSyntaxException extends Exception {

	/**
	 * the location
	 */
	private Token location;
	
	/**
	 * the skipLine
	 */
	private boolean skipLine;
	
	/**
	 * Constructor. This constructor skips the current line if the
	 * token isn't a newline token.
	 * @param location the location at which the error occurred
	 * @param message the exception message
	 */
	public LineSyntaxException(Token location, String message) {
		super(message);
		this.location = location;
		this.skipLine = (location.getCode() != Token.TOKEN_NEWLINE);
	}

	/**
	 * Constructor.
	 * @param location the location at which the error occurred
	 * @param message the exception message
	 * @param skipLine whether to skip the current line. Basically, this should be
	 * true if and only if the end-of-line hasn't yet been read.
	 */
	public LineSyntaxException(Token location, String message, boolean skipLine) {
		super(message);
		this.location = location;
		this.skipLine = skipLine;
	}
	
	/**
	 * Getter method for the location.
	 * @return the location
	 */
	public Token getLocation() {
		return location;
	}
	
	/**
	 * Getter method for the skipLine.
	 * @return the skipLine
	 */
	public boolean isSkipLine() {
		return skipLine;
	}
	
	/**
	 * Reports this exception to the specified error handler.
	 * @param errorHandler the error handler to report to
	 */
	public void report(IPicoblazeAssemblerErrorHandler errorHandler) {
		errorHandler.handleError(location, getMessage());
	}
	
}
