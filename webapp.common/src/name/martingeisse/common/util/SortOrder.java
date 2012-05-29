/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * Enum type for ascending / descending sort order.
 */
public enum SortOrder {

	/**
	 * sort in ascending order
	 */
	ASCENDING,
	
	/**
	 * sort in descending order
	 */
	DESCENDING;

	/**
	 * Returns the matching constant from the specified string, using NAME as the fallback if none matches.
	 * @param s the string that contains the constant name
	 * @return the constant
	 */
	public static SortOrder fromString(String s) {
		return EnumUtil.fromString(SortOrder.class, s, ASCENDING);
	}
	
}
