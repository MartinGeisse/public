/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript;

/**
 * This class contains utility methods for Javascript assembling.
 */
public class JavascriptAssemblerUtil {

	/**
	 * Returns the argument, formatted as a Javascript identifier.
	 * This method actually just returns the argument since identifiers
	 * do not use any special syntax.
	 * @param identifier the identifier
	 * @return the formatted identifier
	 */
	public static String formatIdentifier(String identifier) {
		if (identifier == null) {
			throw new IllegalArgumentException("identifier argument is null");
		}
		return identifier;
	}
	
	/**
	 * Appends the argument, formatted as a Javascript identifier,
	 * to the string builder. This method actually just appends
	 * the argument since identifiers do not use any special syntax.
	 * @param stringBuilder the string builder
	 * @param identifier the identifier
	 */
	public static void appendIdentifier(StringBuilder stringBuilder, String identifier) {
		if (stringBuilder == null) {
			throw new IllegalArgumentException("stringBuilder argument is null");
		}
		if (identifier == null) {
			throw new IllegalArgumentException("identifier argument is null");
		}
		stringBuilder.append(identifier);
	}
	
	/**
	 * Returns the argument with any characters escaped that have a
	 * special meaning inside Javascript string literals. This allows
	 * to use the returned string as part of such a string literal.
	 * @param value a string that possibly contains special characters
	 * @return the argument with all special characters escaped
	 */
	public static String escapeStringLiteralSpecialCharacters(String value) {
		if (value == null) {
			throw new IllegalArgumentException("identifier argument is null");
		}
		StringBuilder builder = new StringBuilder();
		for (int i=0; i<value.length(); i++) {
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
	 * Returns the argument, formatted as a Javascript string literal.
	 * This method adds quotes and escapes all special characters.
	 * @param value the unformatted string literal value
	 * @return the formatted string literal expression
	 */
	public static String formatStringLiteral(String value) {
		if (value == null) {
			throw new IllegalArgumentException("identifier argument is null");
		}
		return "\"" + escapeStringLiteralSpecialCharacters(value) + "\"";
	}
	
	/**
	 * Appends the argument, formatted as a Javascript string literal,
	 * to the string builder. This method adds quotes and escapes all
	 * special characters.
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

	/**
	 * Returns the argument, formatted as a Javascript boolean literal.
	 * @param value the value of the literal
	 * @return the formatted literal expression
	 */
	public static String formatBooleanLiteral(boolean value) {
		return (value ? "true" : "false");
	}
	
	/**
	 * Appends the argument, formatted as a Javascript boolean literal.
	 * @param stringBuilder the string builder
	 * @param value the value of the literal
	 */
	public static void appendBooleanLiteral(StringBuilder stringBuilder, boolean value) {
		if (stringBuilder == null) {
			throw new IllegalArgumentException("stringBuilder argument is null");
		}
		stringBuilder.append(formatBooleanLiteral(value));
	}
	
}
