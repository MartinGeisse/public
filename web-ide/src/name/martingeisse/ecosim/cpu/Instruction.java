/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;

/**
 * This class contains masks and constants for instruction decoding.
 * Opcode constants listed here are the values that appear when the
 * actual instruction is masked and shifted.
 */
public class Instruction {

	/**
	 * the OPCODE_VALUE_MASK
	 */
	public static final int OPCODE_VALUE_MASK = 0x3f;

	/**
	 * the OPCODE_SHIFT
	 */
	public static final int OPCODE_SHIFT = 26;

	/**
	 * the OPCODE_INSTRUCTION_MASK
	 */
	public static final int OPCODE_INSTRUCTION_MASK = OPCODE_VALUE_MASK << OPCODE_SHIFT;

	/**
	 * the REG1_VALUE_MASK
	 */
	public static final int REG1_VALUE_MASK = 0x1f;

	/**
	 * the REG1_SHIFT
	 */
	public static final int REG1_SHIFT = 21;

	/**
	 * the REG1_INSTRUCTION_MASK
	 */
	public static final int REG1_INSTRUCTION_MASK = REG1_VALUE_MASK << REG1_SHIFT;

	/**
	 * the REG2_VALUE_MASK
	 */
	public static final int REG2_VALUE_MASK = 0x1f;

	/**
	 * the REG2_SHIFT
	 */
	public static final int REG2_SHIFT = 16;

	/**
	 * the REG2_INSTRUCTION_MASK
	 */
	public static final int REG2_INSTRUCTION_MASK = REG2_VALUE_MASK << REG2_SHIFT;

	/**
	 * the REG3_VALUE_MASK
	 */
	public static final int REG3_VALUE_MASK = 0x1f;

	/**
	 * the REG3_SHIFT
	 */
	public static final int REG3_SHIFT = 11;

	/**
	 * the REG3_INSTRUCTION_MASK
	 */
	public static final int REG3_INSTRUCTION_MASK = REG3_VALUE_MASK << REG3_SHIFT;

	/**
	 * the IMMEDIATE_VALUE_MASK
	 */
	public static final int IMMEDIATE_VALUE_MASK = 0xffff;

	/**
	 * the IMMEDIATE_SHIFT
	 */
	public static final int IMMEDIATE_SHIFT = 0;

	/**
	 * the IMMEDIATE_INSTRUCTION_MASK
	 */
	public static final int IMMEDIATE_INSTRUCTION_MASK = IMMEDIATE_VALUE_MASK << IMMEDIATE_SHIFT;

	/**
	 * the OFFSET_VALUE_MASK
	 */
	public static final int OFFSET_VALUE_MASK = 0x03FFFFFF;

	/**
	 * the OFFSET_SHIFT
	 */
	public static final int OFFSET_SHIFT = 0;

	/**
	 * the OFFSET_INSTRUCTION_MASK
	 */
	public static final int OFFSET_INSTRUCTION_MASK = OFFSET_VALUE_MASK << OFFSET_SHIFT;

	/**
	 * the OPCODE_ADD
	 */
	public static final int OPCODE_ADD = 0x00;

	/**
	 * the OPCODE_ADDI
	 */
	public static final int OPCODE_ADDI = 0x01;
	
	/**
	 * the OPCODE_SUB
	 */
	public static final int OPCODE_SUB = 0x02;
	
	/**
	 * the OPCODE_SUBI
	 */
	public static final int OPCODE_SUBI = 0x03;

	/**
	 * the OPCODE_MUL
	 */
	public static final int OPCODE_MUL = 0x04;
	
	/**
	 * the OPCODE_MULI
	 */
	public static final int OPCODE_MULI = 0x05;
	
	/**
	 * the OPCODE_MULU
	 */
	public static final int OPCODE_MULU = 0x06;
	
	/**
	 * the OPCODE_MULUI
	 */
	public static final int OPCODE_MULUI = 0x07;
	
	/**
	 * the OPCODE_DIV
	 */
	public static final int OPCODE_DIV = 0x08;
	
	/**
	 * the OPCODE_DIVI
	 */
	public static final int OPCODE_DIVI = 0x09;
	
	/**
	 * the OPCODE_DIVU
	 */
	public static final int OPCODE_DIVU = 0x0A;
	
	/**
	 * the OPCODE_DIVUI
	 */
	public static final int OPCODE_DIVUI = 0x0B;
	
	/**
	 * the OPCODE_REM
	 */
	public static final int OPCODE_REM = 0x0C;
	
	/**
	 * the OPCODE_REMI
	 */
	public static final int OPCODE_REMI = 0x0D;
	
	/**
	 * the OPCODE_REMU
	 */
	public static final int OPCODE_REMU = 0x0E;
	
	/**
	 * the OPCODE_REMUI
	 */
	public static final int OPCODE_REMUI = 0x0F;

	/**
	 * the OPCODE_AND
	 */
	public static final int OPCODE_AND = 0x10;
	
	/**
	 * the OPCODE_ANDI
	 */
	public static final int OPCODE_ANDI = 0x11;
	
	/**
	 * the OPCODE_OR
	 */
	public static final int OPCODE_OR = 0x12;
	
	/**
	 * the OPCODE_ORI
	 */
	public static final int OPCODE_ORI = 0x13;
	
	/**
	 * the OPCODE_XOR
	 */
	public static final int OPCODE_XOR = 0x14;
	
	/**
	 * the OPCODE_XORI
	 */
	public static final int OPCODE_XORI = 0x15;
	
	/**
	 * the OPCODE_XNOR
	 */
	public static final int OPCODE_XNOR = 0x16;
	
	/**
	 * the OPCODE_XNORI
	 */
	public static final int OPCODE_XNORI = 0x17;

	/**
	 * the OPCODE_SLL
	 */
	public static final int OPCODE_SLL = 0x18;
	
	/**
	 * the OPCODE_SLLI
	 */
	public static final int OPCODE_SLLI = 0x19;
	
	/**
	 * the OPCODE_SLR
	 */
	public static final int OPCODE_SLR = 0x1A;
	
	/**
	 * the OPCODE_SLRI
	 */
	public static final int OPCODE_SLRI = 0x1B;
	
	/**
	 * the OPCODE_SAR
	 */
	public static final int OPCODE_SAR = 0x1C;
	
	/**
	 * the OPCODE_SARI
	 */
	public static final int OPCODE_SARI = 0x1D;

	/**
	 * the OPCODE_LDHI
	 */
	public static final int OPCODE_LDHI = 0x1F;

	/**
	 * the OPCODE_BEQ
	 */
	public static final int OPCODE_BEQ = 0x20;
	
	/**
	 * the OPCODE_BNE
	 */
	public static final int OPCODE_BNE = 0x21;
	
	/**
	 * the OPCODE_BLE
	 */
	public static final int OPCODE_BLE = 0x22;
	
	/**
	 * the OPCODE_BLEU
	 */
	public static final int OPCODE_BLEU = 0x23;
	
	/**
	 * the OPCODE_BLT
	 */
	public static final int OPCODE_BLT = 0x24;
	
	/**
	 * the OPCODE_BLTU
	 */
	public static final int OPCODE_BLTU = 0x25;
	
	/**
	 * the OPCODE_BGE
	 */
	public static final int OPCODE_BGE = 0x26;
	
	/**
	 * the OPCODE_BGEU
	 */
	public static final int OPCODE_BGEU = 0x27;
	
	/**
	 * the OPCODE_BGT
	 */
	public static final int OPCODE_BGT = 0x28;
	
	/**
	 * the OPCODE_BGTU
	 */
	public static final int OPCODE_BGTU = 0x29;

	/**
	 * the OPCODE_J
	 */
	public static final int OPCODE_J = 0x2A;
	
	/**
	 * the OPCODE_JR
	 */
	public static final int OPCODE_JR = 0x2B;
	
	/**
	 * the OPCODE_JAL
	 */
	public static final int OPCODE_JAL = 0x2C;
	
	/**
	 * the OPCODE_JALR
	 */
	public static final int OPCODE_JALR = 0x2D;

	/**
	 * the OPCODE_TRAP
	 */
	public static final int OPCODE_TRAP = 0x2E;

	/**
	 * the OPCODE_RFX
	 */
	public static final int OPCODE_RFX = 0x2F;

	/**
	 * the OPCODE_LDW
	 */
	public static final int OPCODE_LDW = 0x30;
	
	/**
	 * the OPCODE_LDH
	 */
	public static final int OPCODE_LDH = 0x31;
	
	/**
	 * the OPCODE_LDHU
	 */
	public static final int OPCODE_LDHU = 0x32;
	
	/**
	 * the OPCODE_LDB
	 */
	public static final int OPCODE_LDB = 0x33;
	
	/**
	 * the OPCODE_LDBU
	 */
	public static final int OPCODE_LDBU = 0x34;

	/**
	 * the OPCODE_STW
	 */
	public static final int OPCODE_STW = 0x35;
	
	/**
	 * the OPCODE_STH
	 */
	public static final int OPCODE_STH = 0x36;
	
	/**
	 * the OPCODE_STB
	 */
	public static final int OPCODE_STB = 0x37;

	/**
	 * the OPCODE_MVFS
	 */
	public static final int OPCODE_MVFS = 0x38;
	
	/**
	 * the OPCODE_MVTS
	 */
	public static final int OPCODE_MVTS = 0x39;
	
	/**
	 * the OPCODE_TBS
	 */
	public static final int OPCODE_TBS = 0x3A;
	
	/**
	 * the OPCODE_TBWR
	 */
	public static final int OPCODE_TBWR = 0x3B;
	
	/**
	 * the OPCODE_TBRI
	 */
	public static final int OPCODE_TBRI = 0x3C;
	
	/**
	 * the OPCODE_TBWI
	 */
	public static final int OPCODE_TBWI = 0x3D;

	/**
	 * Prevent instantiation.
	 */
	private Instruction() {
	}
	
}
