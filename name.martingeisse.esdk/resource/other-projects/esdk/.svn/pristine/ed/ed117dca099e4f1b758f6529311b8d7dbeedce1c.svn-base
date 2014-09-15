package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * Sub-parser for register renaming.
 */
public class NameregParser extends DirectiveParser {

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {
		final Token oldName = tokenizer.getToken();
		if (oldName.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(oldName, "old register identifier");
		}
		parser.expectComma();
		final Token newName = tokenizer.getToken();
		if (newName.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(newName, "new register identifier");
		}
		parser.expectNewline();
		client.namereg(new Range(location, newName), oldName, newName, oldName.getIdentifier(), newName.getIdentifier());
	}

}
