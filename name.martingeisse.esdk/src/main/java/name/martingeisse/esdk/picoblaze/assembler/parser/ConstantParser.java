package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * Sub-parser for constant definitions.
 */
public class ConstantParser extends DirectiveParser {

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {
		final Token name = tokenizer.getToken();
		if (name.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(name, "constant identifier");
		}
		parser.expectComma();
		final Token value = tokenizer.getToken();
		if (value.getCode() != Token.TOKEN_NUMBER) {
			parser.expected(value, "two-digit constant value");
		}
		parser.expectNewline();
		client.constant(new Range(location, value), name, value, name.getIdentifier(), value.getNumber());
	}

}
