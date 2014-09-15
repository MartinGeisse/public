package name.martingeisse.esdk.picoblaze.assembler.parser;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * This interface is implemented by code that uses the .psm parser.
 * The parser invokes methods from this interface to indicate to
 * the client what has been parsed.
 */
public interface IParserClient {

	/**
	 * This method is invoked when a label has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param nameRange the syntactic range that contains the name of the label, or null if unknown
	 * @param name the name of the label
	 */
	public void label(Range fullRange, Range nameRange, String name);

	/**
	 * This method is invoked when a register renaming has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param oldNameRange the syntactic range that contains the old name of the register, or null if unknown
	 * @param newNameRange the syntactic range that contains the new name of the register, or null if unknown
	 * @param oldName the old name of the register
	 * @param newName the new name of the register
	 */
	public void namereg(Range fullRange, Range oldNameRange, Range newNameRange, String oldName, String newName);

	/**
	 * This method is invoked when a constant definition has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param nameRange the syntactic range that contains the name of the constant, or null if unknown
	 * @param valueRange the syntactic range that contains the value of the constant, or null if unknown
	 * @param name the name of the constant
	 * @param value the value of the constant
	 */
	public void constant(Range fullRange, Range nameRange, Range valueRange, String name, int value);

	/**
	 * This method is invoked when a assembling address directive has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param addressRange the syntactic range that contains the address, or null if unknown
	 * @param a the specified address
	 */
	public void address(Range fullRange, Range addressRange, int a);

	/**
	 * This method is invoked when an RR-type instruction has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param leftOperandRange the syntactic range that contains the left operand, or null if unknown
	 * @param rightOperandRange the syntactic range that contains the left operand, or null if unknown
	 * @param opcode the instruction opcode 
	 * @param reg1 the left register name
	 * @param reg2 the right register name
	 */
	public void instructionRR(Range fullRange, Range leftOperandRange, Range rightOperandRange, int opcode, String reg1, String reg2);

	/**
	 * This method is invoked when an RI-type instruction has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param leftOperandRange the syntactic range that contains the left operand, or null if unknown
	 * @param rightOperandRange the syntactic range that contains the left operand, or null if unknown
	 * @param opcode the instruction opcode 
	 * @param reg the (left) register name
	 * @param immediate the (right) immediate operand value
	 */
	public void instructionRI(Range fullRange, Range leftOperandRange, Range rightOperandRange, int opcode, String reg, int immediate);

	/**
	 * This method is invoked when an R-type instruction has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param operandRange the syntactic range that contains the operand, or null if unknown
	 * @param opcode the instruction opcode 
	 * @param reg the register name
	 */
	public void instructionR(Range fullRange, Range operandRange, int opcode, String reg);

	/**
	 * This method is invoked when a J-type instruction has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param conditionRange the syntactic range that contains the condition, or null if unknown
	 * @param targetRange the syntactic range that contains the jump target, or null if unknown
	 * @param opcode the instruction opcode 
	 * @param condition the jump condition code
	 * @param target the target label of the jump
	 */
	public void instructionJ(Range fullRange, Range conditionRange, Range targetRange, int opcode, int condition, String target);

	/**
	 * This method is invoked when a N-type instruction has been parsed.
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param opcode the instruction opcode 
	 */
	public void instructionN(Range fullRange, int opcode);

	/**
	 * This method is invoked when a pragma comment has been parsed. A pragma comment is
	 * a comment in a line on its own (i.e. with no preceding non-comment stuff) that
	 * starts with an at-sign followed directly (without intermediate whitespace) by
	 * a pragma identifier, then optionally whitespace and a parameter string.
	 * 
	 * The pragma identifier may contain uppercase and lowercase letters, digits, underscore
	 * and dot characters.
	 * 
	 * The parameter string is trimmed. If this yields the empty string, null is passed instead.
	 * 
	 * @param fullRange the full syntactic range, or null if unknown
	 * @param identifier the pragma identifier
	 * @param parameter the parameter string, or null if empty
	 */
	public void pragma(Range fullRange, String identifier, String parameter);
	
}
