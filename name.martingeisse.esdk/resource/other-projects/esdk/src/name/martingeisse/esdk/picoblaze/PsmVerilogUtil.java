/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze;

import java.io.IOException;

/**
 * Utility methods to generate Verilog code from PSM instructions.
 * 
 * Block RAM initialization: We have 1024 instructions, with 18 bits each.
 * The Block RAM is initialized as (64*256 data bits + 8*256 parity bits).
 * Each instruction is stored in the block RAM as 16 contiguous data bits
 * and 2 contiguous parity bits, so the organization could be written as:
 * 
 *   (64 * 16 * 16 + 8 * 128 * 2) bits
 *   
 * The initialization is written in hex digits, each comprising 4 bits. The
 * hex digits in one row are written in reverse order, with 64 data rows
 * and 8 parity rows. This yields:
 * 
 *   data:
 *     64 rows
 *     16 instructions per row
 *     4 digits per instruction
 *     4 bits per digit
 *     
 *   parity:
 *     8 rows
 *     64 double-instructions per row
 *     1 digit per double-instruction
 *     4 bits per digit
 *
 */
public class PsmVerilogUtil {
	
	static void writeDigit(StringBuilder builder, int digit) {
		if (digit < 10) {
			builder.append((char)('0' + digit));
		} else {
			builder.append((char)('A' + digit - 10));
		}
	}
	
	static void writeDataRow(StringBuilder builder, int[] instructions, int rowNumber) throws IllegalArgumentException, IOException {
		for (int localInstructionIndex = 15; localInstructionIndex >= 0; localInstructionIndex--) {
			int globalInstructionIndex = (rowNumber * 16 + localInstructionIndex);
			int instruction = (instructions[globalInstructionIndex] & 0x3ffff);
			for (int instructionDigitIndex = 3; instructionDigitIndex >= 0; instructionDigitIndex--) {
				int instructionDigit = ((instruction >> (instructionDigitIndex * 4)) & 15);
				writeDigit(builder, instructionDigit);
			}
		}
	}
	
	static void writeParityRow(StringBuilder builder, int[] instructions, int rowNumber) throws IllegalArgumentException, IOException {
		for (int localDoubleInstructionIndex = 63; localDoubleInstructionIndex >= 0; localDoubleInstructionIndex--) {
			int instruction1 = (instructions[(rowNumber * 64 + localDoubleInstructionIndex) * 2 + 0] & 0x3ffff);
			int instruction2 = (instructions[(rowNumber * 64 + localDoubleInstructionIndex) * 2 + 1] & 0x3ffff);
			int instructionBits1 = ((instruction1 >> 16) & 3);
			int instructionBits2 = ((instruction2 >> 16) & 3);
			int digit = (instructionBits2 << 2) + instructionBits1;
			writeDigit(builder, digit);
		}
	}
	
	static void writeRows(StringBuilder builder, int[] instructions, boolean parity, String preRowNumber, String preContents, String postContents) throws IllegalArgumentException, IOException {
		int rowCount = (parity ? 8 : 64);
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			builder.append(preRowNumber);
			writeDigit(builder, rowIndex >> 4);
			writeDigit(builder, rowIndex & 15);
			builder.append(preContents);
			if (parity) {
				writeParityRow(builder, instructions, rowIndex);
			} else {
				writeDataRow(builder, instructions, rowIndex);
			}
			builder.append(postContents);
			builder.append('\n');
		}
	}

	/**
	 * Generates a verilog file for a PSM bundle (PicoBlaze and program memory).
	 * 
	 * @param instructions the encoded isntructions. The size of this array must be 1024.
	 * @return the verilog code
	 * @throws IllegalArgumentException when the instructions argument is
	 * null or does not have exactly 1024 elements.
	 * @throws IOException on I/O errors
	 */
	public static String generateBundleVerilog(int[] instructions) throws IllegalArgumentException, IOException {
		
		// argument check
		if (instructions == null) {
			throw new IllegalArgumentException("instructions argument is null");
		}
		if (instructions.length != 1024) {
			throw new IllegalArgumentException("instructions argument has " + instructions.length + " elements, 1024 expected");
		}
		
		// generate the code
		StringBuilder builder = new StringBuilder();
		builder.append("`default_nettype none\n");
		builder.append("`timescale 1ns / 1ps\n");
		builder.append("\n");
		builder.append("/**\n");
		builder.append(" * This is a PicoBlaze program memory.\n");
		builder.append(" */\n");
		builder.append("module ProgramMemory (\n");
		builder.append("\t\t\n");
		builder.append("\t\t/** the clock signal **/\n");
		builder.append("\t\tinput [9:0] clk,\n");
		builder.append("\t\t\n");
		builder.append("\t\t/** the current instruction address **/\n");
		builder.append("\t\tinput [9:0] address,\n");
		builder.append("\t\t\n");
		builder.append("\t\t/** the instruction **/\n");
		builder.append("\t\toutput [17:0] instruction\n");
		builder.append("\t\t\n");
		builder.append("\t);\n");
		builder.append("\t\n");
		builder.append("\tRAMB16_S18 blockRam(\n");
		builder.append("\t\t.DI (16'h0000),");
		builder.append("\t\t.DIP (2'b00),");
		builder.append("\t\t.EN (1'b1),");
		builder.append("\t\t.WE (1'b0),");
		builder.append("\t\t.SSR (1'b0),");
		builder.append("\t\t.CLK (clk),");
		builder.append("\t\t.ADDR (address),");
		builder.append("\t\t.DO (instruction[15:0]),");
		builder.append("\t\t.DOP (instruction[17:16])");
		builder.append("\t/*synthesis\n");
		writeRows(builder, instructions, false, "init_", " = \"", "\"");
		writeRows(builder, instructions, true, "initp_", " = \"", "\"");
		builder.append("*/;\n");
		builder.append("\n");
		builder.append("// synthesis translate_off\n");
		writeRows(builder, instructions, false, "defparam blockRam.INIT_", " = 256'h", ";");
		writeRows(builder, instructions, true, "defparam blockRam.INITP_", " = 256'h", ";");
		builder.append("\n");
		builder.append("// synthesis translate_on\n");
		writeRows(builder, instructions, false, "// synthesis attribute INIT_", " of blockRam is \"", "\"");
		writeRows(builder, instructions, true, "// synthesis attribute INITP_", " of blockRam is \"", "\"");
		builder.append("\n");
		builder.append("endmodule\n");
		
		return builder.toString();
		
	}

}
