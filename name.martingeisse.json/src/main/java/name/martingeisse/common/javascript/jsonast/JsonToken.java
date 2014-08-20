/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * A token that gets passed from the {@link JsonLexer} to the {@link AbstractJsonParser}.
 */
enum JsonToken {

	/**
	 * null, true, false
	 */
	KEYWORD,
	
	/**
	 * integer numbers
	 */
	INTEGER,
	
	/**
	 * floating-point numbers
	 */
	FLOAT,
	
	/**
	 * strings
	 */
	STRING,
	
	/**
	 * starts an array
	 */
	OPENING_SQUARE_BRACKET,
	
	/**
	 * ends an array
	 */
	CLOSING_SQUARE_BRACKET,
	
	/**
	 * starts an object
	 */
	OPENING_CURLY_BRACE,
	
	/**
	 * ends an object
	 */
	CLOSING_CURLY_BRACE,
	
	/**
	 * separates array elements or object properties
	 */
	COMMA,
	
	/**
	 * separates object property names from their values
	 */
	COLON,
	
	/**
	 * special token that gets inserted at the end-of-file
	 */
	EOF

}
