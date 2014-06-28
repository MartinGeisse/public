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
	 * Checks whether the specified value is a scalar (i.e. an instance of one of: any class that
	 * implements {@link Number}; {@link Boolean}, or {@link String}). Note that null is not
	 * considered to be a scalar.
	 * 
	 * @param value the value
	 * @return true if the value is a scalar, false if not
	 */
	public static boolean isScalar(Object value) {
		return ((value instanceof Number) || (value instanceof Boolean) || (value instanceof String));
	}
	
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
	 * Checks whether the specified value is empty according to PHP's empty().
	 * @param value the value
	 * @return true if empty, false if not
	 */
	public static boolean empty(Object value) {
		return !convertToBoolean(value);
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

	/**
	 * Maps a string to a byte array by mapping each character to
	 * a byte, keeping only the lowest 8 bits of each character code.
	 * 
	 * @param s the string
	 * @return the byte array
	 */
	public static byte[] mapStringDirectlyToBinary(String s) {
		byte[] result = new byte[s.length()];
		for (int i=0; i<s.length(); i++) {
			result[i] = (byte)s.charAt(i);
		}
		return result;
	}

	/**
	 * This method is used when creating an array by setting an element in a non-array
	 * variable. It takes the value currently stored in the variable, and checks
	 * whether that value allows creating an array this way.
	 * 
	 * @param value the value used in place of an array
	 * @return true if the value can be overwritten by an implicitly constructed
	 * array, false if not
	 */
	public static boolean valueCanBeOverwrittenByImplicitArrayConstruction(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof Boolean) {
			Boolean b = (Boolean)value;
			return (b.booleanValue() == false);
		}
		return false;
	}

}
