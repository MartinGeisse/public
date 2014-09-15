package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * Sub-parser for (register, register/immediate) type instructions.
 * 
 * Note: The second operand may optionally be enclosed in parentheses.
 * This has no effect. The original PicoBlaze assembler seems to use
 * parentheses as a hint for register right operands (as opposed to
 * immediate right operands), BUT:
 * - does so only for some instructions, not for all
 * - does so only for the right operand, not for the left (which is
 * always a register operand and thus would require parentheses all
 * the time).
 * 
 * For these reasons, register names and immediate values must be
 * distinguishable anyway, so the parentheses are redundant.
 * Furthermore, the rules are very inconvenient for a developer who
 * is not used to them. This parser thus allows parentheses (for
 * users who *are* used to them) but does not require them. Using
 * parentheses has no effect other than that they must match.
 */
public class InstructionRXParser extends DirectiveParser {

	/**
	 * the opcode
	 */
	private final int opcode;

	/**
	 * Constructor.
	 * @param opcode the instruction opcode
	 */
	public InstructionRXParser(final int opcode) {
		this.opcode = opcode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {
		
		final Token op1 = tokenizer.getToken();
		if (op1.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(op1, "first register operand");
		}

		parser.expectComma();

		Token op2 = tokenizer.getToken();
		boolean hasParentheses = (op2.getCode() == Token.TOKEN_LPAREN);
		if (hasParentheses) {
			op2 = tokenizer.getToken();
		}
		
		if (op2.getCode() != Token.TOKEN_NUMBER && op2.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(op2, "register or immediate operand");
		}

		Token end;
		if (hasParentheses) {
			end = parser.expectClosingParenthesis();
		} else {
			end = op2;
		}
		
		parser.expectNewline();
		
		Range fullRange = new Range(location, end);
		if (op2.getCode() == Token.TOKEN_NUMBER) {
			client.instructionRI(fullRange, op1, op2, opcode, op1.getIdentifier(), op2.getNumber());
		} else {
			client.instructionRR(fullRange, op1, op2, opcode, op1.getIdentifier(), op2.getIdentifier());
		}
		
	}

}
