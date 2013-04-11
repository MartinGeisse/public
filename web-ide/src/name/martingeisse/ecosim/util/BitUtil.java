/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

/**
 * This class contains bit-fiddling utility methods.
 */
public class BitUtil {

	/**
	 * Returns a value whose lowest 8 bits are the lowest 8 bits of the original
	 * value in reversed order. The higher 24 bits of the result are cleared.
	 * @param originalValue the original value whose bit order shall be reversed
	 * @return Returns the result.
	 */
	public static int revertByteBitOrder(int originalValue) {
		int result = 0;
		for (int i=0; i<8; i++) {
			result = (result << 1) + (originalValue & 1);
			originalValue = originalValue >> 1;
		}
		return result;
	}
	
}
