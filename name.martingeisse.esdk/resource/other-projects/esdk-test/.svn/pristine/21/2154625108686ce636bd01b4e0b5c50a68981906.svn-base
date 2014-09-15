/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test case class for {@link PsmBinUtil}.
 */
public class PsmBinUtilTest {

	/**
	 * @throws Exception on errors
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEncodePsmBinNull() throws Exception {
		PsmBinUtil.encodePsmBin(null);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEncodePsmBinWrongSize() throws Exception {
		PsmBinUtil.encodePsmBin(new int[1023]);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testEncodePsmBin() throws Exception {
		int[] instructions = new int[1024];
		instructions[0] = 0x12345678;
		instructions[1] = 0x11223344;
		instructions[1023] = 0xabcd0123;
		byte[] encoded = PsmBinUtil.encodePsmBin(instructions);
		assertEquals(4096, encoded.length);
		assertEquals(0x12, encoded[0] & 0xff);
		assertEquals(0x34, encoded[1] & 0xff);
		assertEquals(0x56, encoded[2] & 0xff);
		assertEquals(0x78, encoded[3] & 0xff);
		assertEquals(0x11, encoded[4] & 0xff);
		assertEquals(0x22, encoded[5] & 0xff);
		assertEquals(0x33, encoded[6] & 0xff);
		assertEquals(0x44, encoded[7] & 0xff);
		assertEquals(0xab, encoded[4092] & 0xff);
		assertEquals(0xcd, encoded[4093] & 0xff);
		assertEquals(0x01, encoded[4094] & 0xff);
		assertEquals(0x23, encoded[4095] & 0xff);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDecodePsmBinNull() throws Exception {
		PsmBinUtil.decodePsmBin(null);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testDecodePsmBinWrongSize() throws Exception {
		PsmBinUtil.decodePsmBin(new byte[4095]);
	}

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testDecodePsmBin() throws Exception {
		byte[] encoded = new byte[4096];
		encoded[0] = (byte)0x12;
		encoded[1] = (byte)0x34;
		encoded[2] = (byte)0x56;
		encoded[3] = (byte)0x78;
		encoded[4] = (byte)0x11;
		encoded[5] = (byte)0x22;
		encoded[6] = (byte)0x33;
		encoded[7] = (byte)0x44;
		encoded[4092] = (byte)0xab;
		encoded[4093] = (byte)0xcd;
		encoded[4094] = (byte)0x01;
		encoded[4095] = (byte)0x23;
		int[] instructions = PsmBinUtil.decodePsmBin(encoded);
		assertEquals(1024, instructions.length);
		assertEquals(0x12345678, instructions[0]);
		assertEquals(0x11223344, instructions[1]);
		assertEquals(0xabcd0123, instructions[1023]);
	}
	
}
