/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

/**
 * This class contains utility methods related to hexadecimal number representation.
 */
public class HexNumberUtil {

	/**
	 * Interprets the lowest 8 bits of the specified value as an
	 * unsigned integer number and renders that number as a two-digit
	 * hex string.
	 * @param value the value to render
	 * @return Returns the rendered value
	 */
	public static String unsignedByteToString(int value) {
		return String.format("%02x", value & 0xff);
	}

	/**
	 * Interprets the lowest 16 bits of the specified value as an
	 * unsigned integer number and renders that number as a four-digit
	 * hex string.
	 * @param value the value to render
	 * @return Returns the rendered value
	 */
	public static String unsignedHalfwordToString(int value) {
		return String.format("%04x", value & 0xffff);
	}

	/**
	 * Interprets the 32 bits of the specified value as an
	 * unsigned integer number and renders that number as an eight-digit
	 * hex string.
	 * @param value the value to render
	 * @return Returns the rendered value
	 */
	public static String unsignedWordToString(int value) {
		return String.format("%08x", value);
	}

}
