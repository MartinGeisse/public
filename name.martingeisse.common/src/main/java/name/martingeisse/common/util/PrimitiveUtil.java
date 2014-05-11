/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * Utility methods to deal with primitive values.
 */
public final class PrimitiveUtil {

	/**
	 * Prevent instantiation.
	 */
	private PrimitiveUtil() {
	}

	/**
	 * Returns value1 if non-null, value2 otherwise.
	 * @param value1 the first value
	 * @param value2 the second value
	 * @return one of the input values.
	 */
	public static boolean fallback(final Boolean value1, final boolean value2) {
		return (value1 != null ? value1 : value2);
	}

	/**
	 * Returns value1 if non-null, value2 otherwise.
	 * @param value1 the first value
	 * @param value2 the second value
	 * @return one of the input values.
	 */
	public static int fallback(final Integer value1, final int value2) {
		return (value1 != null ? value1 : value2);
	}

	/**
	 * Returns value1 if non-null, value2 otherwise.
	 * @param value1 the first value
	 * @param value2 the second value
	 * @return one of the input values.
	 */
	public static long fallback(final Long value1, final long value2) {
		return (value1 != null ? value1 : value2);
	}

	/**
	 * Returns value1 if non-null, value2 otherwise.
	 * @param value1 the first value
	 * @param value2 the second value
	 * @return one of the input values.
	 */
	public static float fallback(final Float value1, final float value2) {
		return (value1 != null ? value1 : value2);
	}

	/**
	 * Returns value1 if non-null, value2 otherwise.
	 * @param value1 the first value
	 * @param value2 the second value
	 * @return one of the input values.
	 */
	public static double fallback(final Double value1, final double value2) {
		return (value1 != null ? value1 : value2);
	}

}
