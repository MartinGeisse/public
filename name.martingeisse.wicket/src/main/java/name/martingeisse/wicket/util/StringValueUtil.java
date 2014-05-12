/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * Utilities to deal with Wicket's {@link StringValue}.
 */
public class StringValueUtil {
	
	/**
	 * Prevent instantiation.
	 */
	private StringValueUtil() {
	}
	
	// ------------------------------------------------------------------------------------------------------------------------
	// Utility methods to deal with (int) values
	// ------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Decodes an integer parameter, returning null if the value is empty or malformed.
	 * @param value the value to decode
	 * @return the decoded value or null
	 */
	public static Integer getOptionalInteger(StringValue value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		try {
			return value.toInt(); 
		} catch (StringValueConversionException e) {
			return null;
		}
	}
	
	/**
	 * Decodes an integer parameter with the specified lower cap. Returns null
	 * if the value is empty or malformed. Returns the lower cap if the value
	 * is less.
	 * 
	 * @param value the value
	 * @param lowerCap the lower bound for the value
	 * @return the decoded value or null.
	 */
	public static Integer getOptionalLowerCappedInteger(StringValue value, int lowerCap) {
		Integer decoded = getOptionalInteger(value);
		return (decoded == null ? null : decoded < lowerCap ? lowerCap : decoded);
	}
	
	/**
	 * Decodes an integer parameter with the specified upper cap. Returns null
	 * if the value is empty or malformed. Returns the upper cap if the value
	 * is greater.
	 * 
	 * @param value the value
	 * @param upperCap the upper bound for the value
	 * @return the decoded value or null.
	 */
	public static Integer getOptionalUpperCappedInteger(StringValue value, int upperCap) {
		Integer decoded = getOptionalInteger(value);
		return (decoded == null ? null : decoded > upperCap ? upperCap : decoded);
	}
	
	/**
	 * Decodes an integer parameter with the specified lower and upper cap. Returns null
	 * if the value is empty or malformed. Returns the respective cap if the value is
	 * outside the allowed range.
	 * 
	 * @param value the value
	 * @param lowerCap the lower bound for the value
	 * @param upperCap the upper bound for the value
	 * @return the decoded value or null.
	 */
	public static Integer getOptionaRangeCappedInteger(StringValue value, int lowerCap, int upperCap) {
		Integer decoded = getOptionalInteger(value);
		return (decoded == null ? null : decoded < lowerCap ? lowerCap : decoded > upperCap ? upperCap : decoded);
	}
	
	/**
	 * Decodes an integer parameter with the specified lower cap. Throws a
	 * {@link StringValueConversionException} if the value is empty, malformed,
	 * or less than the lower cap.
	 * 
	 * @param value the value
	 * @param lowerCap the lower bound for the value
	 * @return the decoded value
	 */
	public static Integer getMandatoryLowerCappedInteger(StringValue value, int lowerCap) {
		return getMandatoryRangeCappedInteger(value, lowerCap, Integer.MAX_VALUE);
	}
	
	/**
	 * Decodes an integer parameter with the specified upper cap. Throws a
	 * {@link StringValueConversionException} if the value is empty, malformed,
	 * or greater than the upper cap.
	 * 
	 * @param value the value
	 * @param upperCap the upper bound for the value
	 * @return the decoded value
	 */
	public static Integer getMandatoryUpperCappedInteger(StringValue value, int upperCap) {
		return getMandatoryRangeCappedInteger(value, Integer.MIN_VALUE, upperCap);
	}
	
	/**
	 * Decodes an integer parameter with the specified lower and upper cap. Throws a
	 * {@link StringValueConversionException} if the value is empty, malformed,
	 * or outside the allowed range. 
	 * 
	 * @param value the value
	 * @param lowerCap the lower bound for the value
	 * @param upperCap the upper bound for the value
	 * @return the decoded value
	 */
	public static Integer getMandatoryRangeCappedInteger(StringValue value, int lowerCap, int upperCap) {
		int decoded = value.toInt();
		if (decoded < lowerCap || decoded > upperCap) {
			String lowerCapText = (lowerCap == Integer.MIN_VALUE ? "*" : Integer.toString(lowerCap));
			String upperCapText = (upperCap == Integer.MAX_VALUE ? "*" : Integer.toString(upperCap));
			throw new StringValueConversionException("value " + decoded + " is outside the allowed range: " + lowerCapText + " .. " + upperCapText);
		}
		return decoded;
	}

	// ------------------------------------------------------------------------------------------------------------------------
	// Utility methods to deal with (String) values
	// ------------------------------------------------------------------------------------------------------------------------

	// ...
	
}
