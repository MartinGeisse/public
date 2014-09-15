/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.assembler;

/**
 * This class defines opcode constants. All constant values are defined
 * "as masked out of the instruction code", i.e. at the same bit position
 * as in the actual encoded instruction.
 */
public class PicoblazeAssemblerOpcodes {

	/**
	 * Opcode for the LOAD instruction.
	 */
	public static final int OPCODE_LOAD = 0x00000;

	/**
	 * Opcode for the ADD instruction.
	 */
	public static final int OPCODE_ADD = 0x18000;

	/**
	 * Opcode for the ADDCY instruction.
	 */
	public static final int OPCODE_ADDCY = 0x1A000;

	/**
	 * Opcode for the SUB instruction.
	 */
	public static final int OPCODE_SUB = 0x1C000;

	/**
	 * Opcode for the SUBCY instruction.
	 */
	public static final int OPCODE_SUBCY = 0x1E000;

	/**
	 * Opcode for the AND instruction.
	 */
	public static final int OPCODE_AND = 0x0A000;

	/**
	 * Opcode for the OR instruction.
	 */
	public static final int OPCODE_OR = 0x0C000;

	/**
	 * Opcode for the XOR instruction.
	 */
	public static final int OPCODE_XOR = 0x0E000;

	/**
	 * Opcode for the COMPARE instruction.
	 */
	public static final int OPCODE_COMPARE = 0x14000;

	/**
	 * Opcode for the TEST instruction.
	 */
	public static final int OPCODE_TEST = 0x12000;

	/**
	 * Opcode for the INPUT instruction.
	 */
	public static final int OPCODE_INPUT = 0x04000;

	/**
	 * Opcode for the OUTPUT instruction.
	 */
	public static final int OPCODE_OUTPUT = 0x2C000;

	/**
	 * Opcode for the STORE instruction.
	 */
	public static final int OPCODE_STORE = 0x2E000;

	/**
	 * Opcode for the FETCH instruction.
	 */
	public static final int OPCODE_FETCH = 0x06000;
	
	/**
	 * Opcode for the RL instruction.
	 */
	public static final int OPCODE_RL = 0x20002;

	/**
	 * Opcode for the RR instruction.
	 */
	public static final int OPCODE_RR = 0x2000C;

	/**
	 * Opcode for the SL0 instruction.
	 */
	public static final int OPCODE_SL0 = 0x20006;

	/**
	 * Opcode for the SL1 instruction.
	 */
	public static final int OPCODE_SL1 = 0x20007;

	/**
	 * Opcode for the SLA instruction.
	 */
	public static final int OPCODE_SLA = 0x20000;

	/**
	 * Opcode for the SLX instruction.
	 */
	public static final int OPCODE_SLX = 0x20004;

	/**
	 * Opcode for the SR0 instruction.
	 */
	public static final int OPCODE_SR0 = 0x2000E;

	/**
	 * Opcode for the SR1 instruction.
	 */
	public static final int OPCODE_SR1 = 0x2000F;

	/**
	 * Opcode for the SRA instruction.
	 */
	public static final int OPCODE_SRA = 0x20008;

	/**
	 * Opcode for the SRX instruction.
	 */
	public static final int OPCODE_SRX = 0x2000A;
	
	/**
	 * Opcode for the JUMP instruction.
	 */
	public static final int OPCODE_JUMP = 0x34000;

	/**
	 * Opcode for the CALL instruction.
	 */
	public static final int OPCODE_CALL = 0x30000;

	/**
	 * Opcode for the RETURN instruction.
	 */
	public static final int OPCODE_RETURN = 0x2A000;

	/**
	 * Condition bits for no condition.
	 */
	public static final int CONDITION_NONE = 0x00000;

	/**
	 * Condition bits for "zero"
	 */
	public static final int CONDITION_Z = 0x01000;

	/**
	 * Condition bits for "not zero"
	 */
	public static final int CONDITION_NZ = 0x01400;

	/**
	 * Condition bits for "carry"
	 */
	public static final int CONDITION_C = 0x01800;

	/**
	 * Condition bits for "not carry"
	 */
	public static final int CONDITION_NC = 0x01C00;	
	
	/**
	 * Opcode for the DISABLE_INTERRUPT instruction.
	 */
	public static final int OPCODE_DISABLE_INTERRUPT = 0x3C000;

	/**
	 * Opcode for the ENABLE_INTERRUPT instruction.
	 */
	public static final int OPCODE_ENABLE_INTERRUPT = 0x3C001;

	/**
	 * Opcode for the RETURNI_DISABLE instruction.
	 */
	public static final int OPCODE_RETURNI_DISABLE = 0x38000;

	/**
	 * Opcode for the RETURNI_ENABLE instruction.
	 */
	public static final int OPCODE_RETURNI_ENABLE = 0x38001;
	
	/**
	 * Prevent instantiation.
	 */
	private PicoblazeAssemblerOpcodes() {
	}
	
}
