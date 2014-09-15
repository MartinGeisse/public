package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * Sub-parser for single-register-type instructions (e.g. shift
 * instructions).
 */
public class InstructionRParser extends DirectiveParser {

	/**
	 * the opcode
	 */
	private final int opcode;

	/**
	 * Constructor.
	 * @param opcode the instruction opcode
	 */
	public InstructionRParser(final int opcode) {
		this.opcode = opcode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {
		final Token op = tokenizer.getToken();
		if (op.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(op, "register operand");
		}
		parser.expectNewline();

		client.instructionR(new Range(location, op), op, opcode, op.getIdentifier());
	}

}
