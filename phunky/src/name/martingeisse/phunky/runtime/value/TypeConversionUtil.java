/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.value;


/**
 * Utility methods for type conversion.
 */
public final class TypeConversionUtil {

	/**
	 * Converts the specified value to a double-precision floating point value.
	 * @param value the original value
	 * @return the converted value
	 */
	public static double convertToDouble(Object value) {
		if (value == null) {
			return 0;
		}
		if (value instanceof Number) {
			Number v = (Number)value;
			return v.doubleValue();
		}
		if (value instanceof String) {
			String v = (String)value;
			if (v.isEmpty()) {
				return 0;
			}
			// TODO parse double *prefix* of the string
			try {
				return Double.parseDouble(v);
			} catch (NumberFormatException e) {
				return 1;
			}
		}
		if (value instanceof Boolean) {
			Boolean v = (Boolean)value;
			return (v ? 1 : 0);
		}
		if (value instanceof PhpArray) {
			PhpArray v = (PhpArray)value;
			return (v.isEmpty() ? 0 : 1);
		}
		return 1;
	}
	
	/**
	 * Converts the specified value to an integer value.
	 * @param value the original value
	 * @return the converted value
	 */
	public static int convertToInt(Object value) {
		if (value == null) {
			return 0;
		}
		if (value instanceof Number) {
			Number v = (Number)value;
			return v.intValue();
		}
		if (value instanceof String) {
			String v = (String)value;
			if (v.isEmpty()) {
				return 0;
			}
			// TODO parse int *prefix* of the string
			try {
				return Integer.parseInt(v);
			} catch (NumberFormatException e) {
				return 1;
			}
		}
		if (value instanceof Boolean) {
			Boolean v = (Boolean)value;
			return (v ? 1 : 0);
		}
		if (value instanceof PhpArray) {
			PhpArray v = (PhpArray)value;
			return (v.isEmpty() ? 0 : 1);
		}
		return 1;
	}

	/**
	 * Converts the specified value to a boolean value.
	 * @param value the original value
	 * @return the converted value
	 */
	public static boolean convertToBoolean(Object value) {
		int intValue = convertToInt(value);
		return (intValue != 0);
	}

	/**
	 * Converts the specified value to a string value.
	 * @param value the original value
	 * @return the converted value
	 */
	public static String convertToString(Object value) {
		if (value == null) {
			return "";
		}
		if (value instanceof Boolean) {
			Boolean v = (Boolean)value;
			return (v ? "1" : "");
		}
		if (value instanceof PhpArray) {
			return "Array";
		}
		return value.toString();
	}
	
}
