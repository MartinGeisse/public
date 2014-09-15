package name.martingeisse.esdk.picoblaze.assembler.ast;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.PicoblazeAssemblerOpcodes;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * An instruction that uses an instruction address operand and
 * condition specifiers. As a special case, the RETURN* instructions
 * are also encoded using this class because they also have
 * condition specifiers, although they do not use an instruction
 * address operand. The corresponding field is <code>null</code>
 * for return instructions.
 * 
 * @author Martin Geisse
 */
public class InstructionJ extends PsmInstruction {

	/**
	 * the conditionRange
	 */
	private final Range conditionRange;

	/**
	 * the targetRange
	 */
	private final Range targetRange;

	/**
	 * The opcode used for this instruction.
	 */
	private final int opcode;

	/**
	 * The condition for the jump to occur.
	 */
	private final int condition;

	/**
	 * Target address of the jump (Integer), none for return (null),
	 * or name of a label (String).
	 */
	private final Object target;

	/**
	 * Creates a new J instruction instance with the specified opcode, condition,
	 * and target. The target must be null for a RETURN instruction, and
	 * an absolute address (Integer) or label name (String) for JUMP and CALL.
	 * @param fullRange the full syntactic range of the renaming, or null if not known
	 * @param conditionRange the syntactic range of the condition
	 * @param targetRange the syntactic range of the jump target
	 * @param opcode the instruction opcode
	 * @param condition the jump condition code
	 * @param target the target address or label
	 */
	public InstructionJ(final Range fullRange, final Range conditionRange, final Range targetRange, final int opcode, final int condition, final Object target) {
		super(fullRange);
		this.conditionRange = conditionRange;
		this.targetRange = targetRange;
		this.opcode = opcode;
		this.condition = condition;
		this.target = target;

		if ((opcode == PicoblazeAssemblerOpcodes.OPCODE_RETURN) != (target == null)) {
			throw new IllegalArgumentException("invalid opcode/target: " + opcode + ", " + target);
		}

		if ((target != null) && !(target instanceof Integer) && !(target instanceof String)) {
			throw new IllegalArgumentException("invalid target: " + target);
		}

	}

	/**
	 * Getter method for the conditionRange.
	 * @return the conditionRange
	 */
	public Range getConditionRange() {
		return conditionRange;
	}

	/**
	 * Getter method for the targetRange.
	 * @return the targetRange
	 */
	public Range getTargetRange() {
		return targetRange;
	}

	/**
	 * Getter method for the opcode.
	 * @return the opcode
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Getter method for the condition.
	 * @return the condition
	 */
	public int getCondition() {
		return condition;
	}

	/**
	 * Getter method for the target.
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.ast.PsmInstruction#encode(name.martingeisse.esdk.picoblaze.assembler.ast.Context, name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler)
	 */
	@Override
	public int encode(final Context context, final IPicoblazeAssemblerErrorHandler errorHandler) {

		int address;
		if (target == null) {
			address = 0;
		} else if (target instanceof Integer) {
			address = (Integer)target;
		} else {
			final String name = (String)target;
			address = context.getLabel(name);
			if (address == -1) {
				noSuchLabel(targetRange, name, errorHandler);
			}
		}

		return opcode + condition + address;
	}
}
