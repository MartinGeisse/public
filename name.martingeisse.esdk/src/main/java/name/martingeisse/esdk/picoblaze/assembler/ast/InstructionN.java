package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * An instruction that uses no operands, i.e. the opcode itself is also the
 * encoded instruction. Note that RETURN* instructions use InstructionJ
 * instead of this class, even though they do not use an operand.
 * 
 * @author Martin Geisse
 */
public class InstructionN extends PsmInstruction {

	/**
	 * The opcode used for this instruction.
	 */
	private final int opcode;

	/**
	 * Creates a new N instruction instance with the specified opcode 
	 * @param fullRange the full syntactic range of the renaming, or null if not known
	 * @param opcode the instruction opcode
	 */
	public InstructionN(final Range fullRange, final int opcode) {
		super(fullRange);
		this.opcode = opcode;
	}

	/**
	 * @return the opcode of this instruction.
	 */
	public int getOpcode() {
		return opcode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.ast.PsmInstruction#encode(name.martingeisse.esdk.picoblaze.assembler.ast.Context, name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler)
	 */
	@Override
	public int encode(final Context context, final IPicoblazeAssemblerErrorHandler errorHandler) {
		return opcode;
	}
	
}
