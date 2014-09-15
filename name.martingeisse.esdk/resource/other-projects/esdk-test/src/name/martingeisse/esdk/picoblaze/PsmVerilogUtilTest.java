/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * TODO: document me
 *
 */
public class PsmVerilogUtilTest {

	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testWriteDigit() throws Exception {
		StringBuilder builder = new StringBuilder();
		PsmVerilogUtil.writeDigit(builder, 0);
		assertEquals("0", builder.toString());
		PsmVerilogUtil.writeDigit(builder, 5);
		assertEquals("05", builder.toString());
		PsmVerilogUtil.writeDigit(builder, 12);
		assertEquals("05C", builder.toString());
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testWriteDataRow() throws Exception {
		
		int[] instructions = new int[1024];
		instructions[15] = 0x12345678;
		instructions[11] = 0x11223344;
		instructions[30] = 0x1234abcd;
		
		StringBuilder builder = new StringBuilder();
		PsmVerilogUtil.writeDataRow(builder, instructions, 0);
		assertEquals("5678000000000000334400000000000000000000000000000000000000000000", builder.toString());
		
		builder.setLength(0);
		PsmVerilogUtil.writeDataRow(builder, instructions, 1);
		assertEquals("0000ABCD00000000000000000000000000000000000000000000000000000000", builder.toString());
		
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testWriteParityRow() throws Exception {
		
		int[] instructions = new int[1024];
		instructions[127] = 0xffffffff; // 11
		instructions[126] = 0x00000000; // 00 --> C
		instructions[125] = 0x00000000; // 00
		instructions[124] = 0xffffffff; // 11 --> 3
		instructions[123] = 0xffffffff; // 11
		instructions[122] = 0xffffffff; // 11 --> F
		instructions[121] = 0x00000000; // 00
		instructions[120] = 0x00000000; // 00 --> 0
		instructions[119] = 0x00000000; // 00
		instructions[118] = 0x00010000; // 01 --> 1
		instructions[117] = 0x00010000; // 01 
		instructions[116] = 0x00000000; // 00 --> 4
		instructions[115] = 0x00000000; // 00 
		instructions[114] = 0x00020000; // 10 --> 2
		instructions[113] = 0x00020000; // 10 
		instructions[112] = 0x00000000; // 00 --> 8
		instructions[111] = 0x00040000; // insignificant bit
		instructions[110] = 0x00040000; // insignificant bit
		
		StringBuilder builder = new StringBuilder();
		PsmVerilogUtil.writeParityRow(builder, instructions, 0);
		assertEquals("C3F0142800000000000000000000000000000000000000000000000000000000", builder.toString());

	}
	
}
