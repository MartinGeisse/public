package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * An instruction that can be used in one of three modes:
 * 
 * <ul>
 *   <li>register, byte literal</li>
 *   <li>register, byte constant name</li>
 *   <li>register, register</li>
 * </ul>
 * 
 * This class contains the name of the first register operand, and an
 * object that is either a String or an Integer. If it is a String,
 * then it is either the name of a constant or of a register. An Integer
 * is used as a literal value.
 * 
 * @author Martin Geisse
 */
public class InstructionRX extends PsmInstruction {

	/**
	 * the leftOperandRange
	 */
	private final Range leftOperandRange;

	/**
	 * the rightOperandRange
	 */
	private final Range rightOperandRange;

	/**
	 * The opcode used for this instruction.
	 */
	private final int opcode;

	/**
	 * Name of the first register operand.
	 */
	private final String op1;

	/**
	 * Name of the second register operand (String), or name of a constant (String),
	 * or literal value (Integer).
	 */
	private final Object op2;

	/**
	 * Creates a new RX instruction instance with the specified opcode and first register operand. 
	 * @param fullRange the full syntactic range of the renaming, or null if not known
	 * @param leftOperandRange the syntactic range of the left operand
	 * @param rightOperandRange the syntactic range of the right operand
	 * @param opcode the instruction opcode
	 * @param op1 the left operand register name
	 * @param op2 the right operand register name or immediate value
	 */
	public InstructionRX(final Range fullRange, final Range leftOperandRange, final Range rightOperandRange, final int opcode, final String op1, final Object op2) {
		super(fullRange);
		this.leftOperandRange = leftOperandRange;
		this.rightOperandRange = rightOperandRange;
		this.opcode = opcode;
		this.op1 = op1;
		this.op2 = op2;

		if (!(op2 instanceof String || op2 instanceof Integer)) {
			throw new IllegalArgumentException("illegal second operand: " + op2);
		}
	}

	/**
	 * Getter method for the leftOperandRange.
	 * @return the leftOperandRange
	 */
	public Range getLeftOperandRange() {
		return leftOperandRange;
	}

	/**
	 * Getter method for the rightOperandRange.
	 * @return the rightOperandRange
	 */
	public Range getRightOperandRange() {
		return rightOperandRange;
	}

	/**
	 * Getter method for the opcode.
	 * @return the opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Getter method for the op1.
	 * @return the op1
	 */
	public String getOp1() {
		return op1;
	}

	/**
	 * Getter method for the op2.
	 * @return the op2
	 */
	public Object getOp2() {
		return op2;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.ast.PsmInstruction#encode(name.martingeisse.esdk.picoblaze.assembler.ast.Context, name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler)
	 */
	@Override
	public int encode(final Context context, final IPicoblazeAssemblerErrorHandler errorHandler) {

		final int reg1 = context.getRegister(op1);
		if (reg1 == -1) {
			noSuchRegister(leftOperandRange, op1, errorHandler);
		}

		if (op2 instanceof Integer) {
			return encodeImmediate(reg1, (Integer)op2);
		}

		final String op2s = (String)op2;
		final int constant = context.getConstant(op2s);
		if (constant != -1) {
			return encodeImmediate(reg1, constant);
		}

		final int reg2 = context.getRegister(op2s);
		if (reg2 != -1) {
			return encodeRegister(reg1, reg2);
		}

		noSuchRegisterOrConstant(rightOperandRange, op2s, errorHandler);
		return 0;
	}

	/**
	 * Subroutine to encode with immediate value.
	 */
	private int encodeImmediate(final int reg, final int imm) {
		return opcode + (reg << 8) + imm;
	}

	/**
	 * Subroutine to encode with register value.
	 */
	private int encodeRegister(final int reg1, final int reg2) {
		return opcode + (1 << 12) + (reg1 << 8) + (reg2 << 4);
	}
}
