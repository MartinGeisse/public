/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * Utility methods to deal with arrays.
 */
public class ArrayUtil {

	/**
	 * Prevent instantiation.
	 */
	private ArrayUtil() {
	}

	/**
	 * Converts an array of objects to an array of strings
	 * using {@link #toString()}.
	 * @param objects the objects
	 * @return the strings
	 */
	public static String[] toStringArray(Object[] objects) {
		String[] result = new String[objects.length];
		for (int i=0; i<result.length; i++) {
			result[i] = objects[i].toString();
		}
		return result;
	}
	
}
