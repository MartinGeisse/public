/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

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
	 * @return the underscored string
	 */
	public static String convertUnderscoresToUpperCamelCase(String s) {
		return StringUtils.remove(WordUtils.capitalizeFully(s, UNDERSCORE_ARRAY), '_');
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
	
}
