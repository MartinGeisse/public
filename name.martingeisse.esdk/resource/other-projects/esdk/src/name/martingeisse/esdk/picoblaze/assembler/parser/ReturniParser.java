package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.PicoblazeAssemblerOpcodes;

/**
 * Sub-parser for RETURNI ENABLE/DISABLE instructions.
 */
public class ReturniParser extends DirectiveParser {

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {
		final Token en = tokenizer.getToken();
		if (en.getCode() == Token.TOKEN_IDENTIFIER) {

			if (en.getIdentifier().equalsIgnoreCase("disable")) {
				client.instructionN(location, PicoblazeAssemblerOpcodes.OPCODE_RETURNI_DISABLE);
				parser.expectNewline();
				return;
			}

			if (en.getIdentifier().equalsIgnoreCase("enable")) {
				client.instructionN(location, PicoblazeAssemblerOpcodes.OPCODE_RETURNI_ENABLE);
				parser.expectNewline();
				return;
			}

		}
		parser.expected(en, "ENABLE or DISABLE");
	}

}
