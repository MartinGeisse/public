/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods for enum types.
 */
public class EnumUtil {

	/**
	 * Prevent instantiation.
	 */
	private EnumUtil() {
	}
	
	/**
	 * @param <T> the static enum type
	 * @param enumClass the runtime class for the enum type
	 * @return all possible values of the enum type, as returned by its values()
	 * method, but as a newly created List instead of an array.
	 */
	public static final <T extends Enum<T>> List<T> getValuesAsList(Class<T> enumClass) {
		ArrayList<T> result = new ArrayList<T>();
		for (T value : enumClass.getEnumConstants()) {
			result.add(value);
		}
		return result;
	}

	/**
	 * Returns the enum constant that matches the specified string, or the default value if
	 * none of the constants matches or if the string is null.
	 * @param <T> the enum type
	 * @param enumClass the class object for the enum type
	 * @param s the string that specifies a constant
	 * @param defaultValue the default value to use if none of the constants matches or if s is null
	 * @return the matching constant or the default value
	 */
	public static final <T extends Enum<T>> T fromString(Class<T> enumClass, String s, T defaultValue) {
		try {
			return Enum.valueOf(enumClass, s);
		} catch (IllegalArgumentException e) {
			return defaultValue;
		} catch (NullPointerException e) {
			return defaultValue;
		}
	}
	
}
