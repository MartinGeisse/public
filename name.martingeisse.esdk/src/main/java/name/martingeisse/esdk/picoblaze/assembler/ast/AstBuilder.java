package name.martingeisse.esdk.picoblaze.assembler.ast;

import java.io.IOException;
import java.io.Reader;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;
import name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient;
import name.martingeisse.esdk.picoblaze.assembler.parser.Parser;
import name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer;

/**
 * This class is used to construct an instance of <code>PsmFile</code>.
 * 
 * @author Martin Geisse
 */
public class AstBuilder implements IParserClient {

	/**
	 * the psmFile
	 */
	private final PsmFile psmFile;

	/**
	 * Constructor.
	 */
	public AstBuilder() {
		this.psmFile = new PsmFile();
	}

	/**
	 * Parses the source code from the specified reader.
	 * @param in the reader to read from
	 * @param errorHandler the error handler
	 * @throws IOException on I/O errors
	 */
	public void parse(final Reader in, final IPicoblazeAssemblerErrorHandler errorHandler) throws IOException {
		final Tokenizer tokenizer = new Tokenizer(in, errorHandler);
		final Parser parser = new Parser(tokenizer, errorHandler);
		parser.parse(this);
	}

	/**
	 * Getter method for the result.
	 * @return the result
	 */
	public PsmFile getResult() {
		return psmFile;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#label(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, java.lang.String)
	 */
	@Override
	public void label(final Range fullRange, final Range nameRange, final String name) {
		psmFile.add(new PsmLabel(fullRange, nameRange, name));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#namereg(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, java.lang.String, java.lang.String)
	 */
	@Override
	public void namereg(final Range fullRange, final Range oldNameRange, final Range newNameRange, final String oldName, final String newName) {
		psmFile.add(new PsmNamereg(fullRange, oldNameRange, newNameRange, oldName, newName));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#constant(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, java.lang.String, int)
	 */
	@Override
	public void constant(final Range fullRange, final Range nameRange, final Range valueRange, final String name, final int value) {
		psmFile.add(new PsmConstant(fullRange, nameRange, valueRange, name, value));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#address(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, int)
	 */
	@Override
	public void address(final Range fullRange, final Range addressRange, final int a) {
		psmFile.add(new PsmAddress(fullRange, addressRange, a));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#instructionRR(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, int, java.lang.String, java.lang.String)
	 */
	@Override
	public void instructionRR(final Range fullRange, final Range leftOperandRange, final Range rightOperandRange, final int opcode, final String reg1, final String reg2) {
		psmFile.add(new InstructionRX(fullRange, leftOperandRange, rightOperandRange, opcode, reg1, reg2));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#instructionRI(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, int, java.lang.String, int)
	 */
	@Override
	public void instructionRI(final Range fullRange, final Range leftOperandRange, final Range rightOperandRange, final int opcode, final String reg, final int immediate) {
		psmFile.add(new InstructionRX(fullRange, leftOperandRange, rightOperandRange, opcode, reg, immediate));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#instructionR(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, int, java.lang.String)
	 */
	@Override
	public void instructionR(final Range fullRange, final Range operandRange, final int opcode, final String reg) {
		psmFile.add(new InstructionR(fullRange, operandRange, opcode, reg));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#instructionJ(name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, name.martingeisse.esdk.picoblaze.assembler.Range, int, int, java.lang.String)
	 */
	@Override
	public void instructionJ(final Range fullRange, final Range conditionRange, final Range targetRange, final int opcode, final int condition, final String target) {
		psmFile.add(new InstructionJ(fullRange, conditionRange, targetRange, opcode, condition, target));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#instructionN(name.martingeisse.esdk.picoblaze.assembler.Range, int)
	 */
	@Override
	public void instructionN(final Range range, final int opcode) {
		psmFile.add(new InstructionN(range, opcode));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient#pragma(name.martingeisse.esdk.picoblaze.assembler.Range, java.lang.String, java.lang.String)
	 */
	@Override
	public void pragma(Range fullRange, String identifier, String parameter) {
		// ignore pragma comments by default
	}
	
}
