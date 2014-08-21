/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.parserbase;


/**
 * A token that gets passed from the {@link JsonLexer} to the {@link AbstractJsonParser}.
 */
enum JsonToken {

	/**
	 * null, true, false
	 */
	KEYWORD("keyword"),

	/**
	 * integer numbers
	 */
	INTEGER("integer number"),

	/**
	 * floating-point numbers
	 */
	FLOAT("floating-point number"),

	/**
	 * strings
	 */
	STRING("string"),

	/**
	 * starts an array
	 */
	OPENING_SQUARE_BRACKET("'['"),

	/**
	 * ends an array
	 */
	CLOSING_SQUARE_BRACKET("']'"),

	/**
	 * starts an object
	 */
	OPENING_CURLY_BRACE("'{'"),

	/**
	 * ends an object
	 */
	CLOSING_CURLY_BRACE("'}'"),

	/**
	 * separates array elements or object properties
	 */
	COMMA("','"),

	/**
	 * separates object property names from their values
	 */
	COLON("':'"),

	/**
	 * special token that gets inserted at the end-of-file
	 */
	EOF("end of input");

	/**
	 * the description
	 */
	private final String description;

	/**
	 * Constructor.
	 * @param description a description (used for error messages)
	 */
	private JsonToken(String description) {
		this.description = description;
	}
	
	/**
	 * Getter method for the description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
