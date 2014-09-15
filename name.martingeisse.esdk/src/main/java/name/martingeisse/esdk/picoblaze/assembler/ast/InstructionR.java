package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * An instruction that uses a single register operand at the
 * position of register 1. This is used for shift and rotate instructions.
 * 
 * The opcode in this class contains all bits except the four bits that
 * specify the register operand (which are set to 0 in the constants
 * defined here).
 * 
 * @author Martin Geisse
 */
public class InstructionR extends PsmInstruction {

	/**
	 * the operandRange
	 */
	private final Range operandRange;
	
	/**
	 * The opcode used for this instruction.
	 */
	private final int opcode;

	/**
	 * Name of the register operand.
	 */
	private final String op;

	/**
	 * Creates a new R instruction instance with the specified opcode and register operand. 
	 * @param fullRange the full syntactic range of the renaming, or null if not known
	 * @param operandRange the syntactic range of the operand
	 * @param opcode the instruction opcode
	 * @param op the operand register name
	 */
	public InstructionR(final Range fullRange, final Range operandRange, final int opcode, final String op) {
		super(fullRange);
		this.operandRange = operandRange;
		this.opcode = opcode;
		this.op = op;
	}

	/**
	 * Getter method for the operandRange.
	 * @return the operandRange
	 */
	public Range getOperandRange() {
		return operandRange;
	}
	
	/**
	 * @return the opcode of this instruction.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * @return the register name of the operand.
	 */
	public String getFirstOperand() {
		return op;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.ast.PsmInstruction#encode(name.martingeisse.esdk.picoblaze.assembler.ast.Context, name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler)
	 */
	@Override
	public int encode(final Context context, final IPicoblazeAssemblerErrorHandler errorHandler) {

		final int reg = context.getRegister(op);
		if (reg == -1) {
			noSuchRegister(operandRange, op, errorHandler);
		}

		return opcode + (reg << 8);
	}
}
