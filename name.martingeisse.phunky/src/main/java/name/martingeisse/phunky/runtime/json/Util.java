/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.json;

/**
 * This class contains utility methods for JSON assembling.
 */
class Util {

	/**
	 * Returns the argument with any characters escaped that have a
	 * special meaning inside JSON string literals. This allows
	 * to use the returned string as part of such a string literal.
	 * 
	 * @param value a string that possibly contains special characters
	 * @return the argument with all special characters escaped
	 */
	public static String escapeStringLiteralSpecialCharacters(String value) {
		if (value == null) {
			throw new IllegalArgumentException("identifier argument is null");
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c < 32) {
				if (c == '\t') {
					builder.append("\\t");
				} else if (c == '\r') {
					builder.append("\\r");
				} else if (c == '\n') {
					builder.append("\\n");
				} else if (c == '\b') {
					builder.append("\\b");
				} else {
					// ignore the rest
				}
			} else {
				if (c == '\\' || c == '\"' || c == '/' || c == '\'') {
					builder.append('\\');
				}
				builder.append(c);
			}
		}
		return builder.toString();
	}

	/**
	 * Appends the argument, formatted as a JSON string literal,
	 * to the string builder. This method adds quotes and escapes all
	 * special characters.
	 * 
	 * @param stringBuilder the string builder
	 * @param value the unformatted string literal value
	 */
	public static void appendStringLiteral(StringBuilder stringBuilder, String value) {
		if (stringBuilder == null) {
			throw new IllegalArgumentException("stringBuilder argument is null");
		}
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		}
		stringBuilder.append('\"').append(escapeStringLiteralSpecialCharacters(value)).append('\"');
	}

}
