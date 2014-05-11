/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Additional string utils not present in Apache Commons-Lang.
 */
public final class StringUtil {

	/**
	 * A character array containing only the underscore character
	 */
	private static final char[] UNDERSCORE_ARRAY = {'_'};
	
	/**
	 * Prevent instantiation.
	 */
	private StringUtil() {
	}

	/**
	 * Converts a camel-case name to a lower-case underscored name like
	 * this: "myFooBar" -> "my_foo_bar".
	 * @param s the string to convert
	 * @return the underscored string
	 */
	public static String convertCamelCaseToLowercaseUnderscores(String s) {
		String[] segments = StringUtils.splitByCharacterTypeCamelCase(s);
		for (int i=0; i<segments.length; i++) {
			segments[i] = segments[i].toLowerCase();
		}
		return StringUtils.join(segments, '_');
	}

	/**
	 * Converts an underscored name to an upper-camel-case name like
	 * this: "my_foo_bar" -> "MyFooBar".
	 * @param s the string to convert
	 * @return the upper camel case string
	 */
	public static String convertUnderscoresToUpperCamelCase(String s) {
		return StringUtils.remove(WordUtils.capitalizeFully(s, UNDERSCORE_ARRAY), '_');
	}

	/**
	 * Converts an underscored name to an lower-camel-case name like
	 * this: "my_foo_bar" -> "myFooBar".
	 * @param s the string to convert
	 * @return the lower camel case string
	 */
	public static String convertUnderscoresToLowerCamelCase(String s) {
		if (s.isEmpty()) {
			return s;
		}
		char firstCharacterLowerCase = Character.toLowerCase(s.charAt(0));
		return firstCharacterLowerCase + StringUtils.remove(WordUtils.capitalizeFully(s, UNDERSCORE_ARRAY), '_').substring(1);
	}
	
	/**
	 * Limits the length of the specified input string. If the string fits into
	 * the maxLength then it is returned unchanged. Otherwise, a prefix of the
	 * input string with "..." appended is returned such that the return value
	 * exactly fits the maxLength. This implies that maxLength is at least 3
	 * to fit the ellipsis.
	 * 
	 * This method returns null if the input string is null.
	 * 
	 * @param s the string to limit in length
	 * @param maxLength the maximum length
	 * @return the length-limited string
	 */
	public static String limitLength(String s, int maxLength) {
		if (maxLength < 3) {
			throw new IllegalArgumentException("maxLength too small (must be at least 3): " + maxLength);
		}
		if (s == null) {
			return null;
		} else if (s.length() > maxLength) {
			return s.substring(0, maxLength - 3) + "...";
		} else {
			return s;
		}
	}
	
	/**
	 * Splits the specified string as {@link StringUtils#split(String, char)}
	 * does, then trims the resulting segments.
	 * 
	 * @param s the string to split
	 * @param separator the separator character
	 * @return the trimmed segments
	 */
	public static String[] splitAndTrim(String s, char separator) {
		String[] result = StringUtils.split(s, separator);
		for (int i=0; i<result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}
	
	/**
	 * Joins the specified segments with the separator, but allows segments to be null in
	 * which case they are omitted. If this leaves no segments, this method returns
	 * the empty string.
	 * 
	 * @param separator the separator
	 * @param segments the segments
	 * @return the joined string
	 */
	public static String joinWithHoles(char separator, String... segments) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String segment : segments) {
			if (segment == null) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				builder.append(separator);
			}
			builder.append(segment);
		}
		return builder.toString();
	}
	
	/**
	 * Returns the specified string, with the first character capitalized.
	 * Returns null/empty if the argument is null/empty.
	 * @param s the input string
	 * @return the output string
	 */
	public static String capitalizeFirst(String s) {
		if (s == null) {
			return null;
		} else if (s.isEmpty()) {
			return s;
		} else {
			return Character.toUpperCase(s.charAt(0)) + s.substring(1);
		}
	}
	
}
