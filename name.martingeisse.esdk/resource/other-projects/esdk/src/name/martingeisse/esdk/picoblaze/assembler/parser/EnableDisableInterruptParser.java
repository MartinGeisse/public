package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

/**
 * Sub-parser for ENABLE/DISABLE INTERRUPT instructions.
 */
public class EnableDisableInterruptParser extends DirectiveParser {

	/**
	 * the opcode
	 */
	private final int opcode;

	/**
	 * Constructor.
	 * @param opcode the instruction opcode
	 */
	public EnableDisableInterruptParser(final int opcode) {
		this.opcode = opcode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {
		parser.expectIdentifier("interrupt");
		parser.expectNewline();
		client.instructionN(location, opcode);
	}

}
