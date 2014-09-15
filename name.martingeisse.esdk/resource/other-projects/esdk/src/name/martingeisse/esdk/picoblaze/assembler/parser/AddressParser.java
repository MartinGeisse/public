package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * Sub-parser for assembling location directives.
 */
public class AddressParser extends DirectiveParser {

	/**
	 * Description string for error messages.
	 */
	private static final String DIG3 = "3-digit address constant";

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {

		// expects an identifier since 3-digit numbers aren't treated as numbers,
		// only 2-digit numbers are.
		final Token value = tokenizer.getToken();
		if (value.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(value, DIG3);
		}

		// parse the address constant
		final String addressText = value.getIdentifier();
		if (addressText.length() != 3) {
			parser.expected(value, DIG3);
		}

		final int digit1 = Tokenizer.hexDigitValue(addressText.charAt(0));
		if (digit1 == -1) {
			parser.expected(value, DIG3);
		}

		final int digit2 = Tokenizer.hexDigitValue(addressText.charAt(1));
		if (digit2 == -1) {
			parser.expected(value, DIG3);
		}

		final int digit3 = Tokenizer.hexDigitValue(addressText.charAt(2));
		if (digit3 == -1) {
			parser.expected(value, DIG3);
		}

		final int address = (digit1 << 8) + (digit2 << 4) + digit3;
		if (address > 0x3ff) {
			throw new LineSyntaxException(value, "Address too large (000..3ff)");
		}

		parser.expectNewline();
		client.address(new Range(location, value), value, address);
	}
}
