/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.parserbase;

/**
 * Thrown by the {@link JsonLexerInput} on errors.
 */
final class JsonLexerInputException extends RuntimeException {

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public JsonLexerInputException(String message) {
		super(message);
	}

}