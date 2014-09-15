/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze;

/**
 * Utility methods to deal with PSMBIN file contents (PicoBlaze binaries).
 * 
 * A PSMBIN contains 1024 instructions, each stored in the low 18
 * bits of a 32-bit value that is stored in the file in big-endian
 * byte order. The file data and instruction array sizes are checked.
 * The upper 14 bits are written as 0 and ignored when read.
 */
public class PsmBinUtil {

	/**
	 * Encodes an instruction array as PSMBIN data.
	 * @param instructions the instructions
	 * @return the PSMBIN data
	 * @throws IllegalArgumentException when the instructions argument is
	 * null or does not have exactly 1024 elements.
	 */
	public static byte[] encodePsmBin(int[] instructions) throws IllegalArgumentException {
		
		// argument check
		if (instructions == null) {
			throw new IllegalArgumentException("instructions argument is null");
		}
		if (instructions.length != 1024) {
			throw new IllegalArgumentException("instructions argument has " + instructions.length + " elements, 1024 expected");
		}
		
		// encode
		byte[] result = new byte[4096];
		for (int i=0; i<1024; i++) {
			int instruction = instructions[i];
			result[4*i+0] = (byte)(instruction >> 24);
			result[4*i+1] = (byte)(instruction >> 16);
			result[4*i+2] = (byte)(instruction >> 8);
			result[4*i+3] = (byte)(instruction >> 0);
		}
		return result;
		
	}
	
	/**
	 * Decodes a PSMBIN data array as instructions.
	 * @param data the PSMBIN data
	 * @return the instructions
	 * @throws IllegalArgumentException when the data argument is null or
	 * does not have exactly 4096 elements
	 */
	public static int[] decodePsmBin(byte[] data) throws IllegalArgumentException {
		
		// argument check
		if (data == null) {
			throw new IllegalArgumentException("data argument is null");
		}
		if (data.length != 4096) {
			throw new IllegalArgumentException("data argument has " + data.length + " elements, 4096 expected");
		}
		
		// encode
		int[] result = new int[1024];
		for (int i=0; i<1024; i++) {
			int b0 = (data[4*i+0] & 0xff);
			int b1 = (data[4*i+1] & 0xff);
			int b2 = (data[4*i+2] & 0xff);
			int b3 = (data[4*i+3] & 0xff);
			result[i] = (b0 << 24) + (b1 << 16) + (b2 << 8) + b3;
		}
		return result;		
	}
	
}
