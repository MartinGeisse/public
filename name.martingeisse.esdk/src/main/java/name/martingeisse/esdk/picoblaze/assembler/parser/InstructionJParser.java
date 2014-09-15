package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.PicoblazeAssemblerOpcodes;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * Sub-parser for jump-type instructions.
 */
public class InstructionJParser extends DirectiveParser {

	/**
	 * the opcode
	 */
	private final int opcode;

	/**
	 * Constructor.
	 * @param opcode the instruction opcode
	 */
	public InstructionJParser(final int opcode) {
		this.opcode = opcode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.assembler.parser.DirectiveParser#parse(name.martingeisse.esdk.picoblaze.assembler.parser.Token, name.martingeisse.esdk.picoblaze.assembler.parser.Tokenizer, name.martingeisse.esdk.picoblaze.assembler.parser.Parser, name.martingeisse.esdk.picoblaze.assembler.parser.IParserClient)
	 */
	@Override
	public void parse(final Token location, final Tokenizer tokenizer, final Parser parser, final IParserClient client) throws LineSyntaxException, IOException {
		// return instructions have get special treatment due to the missing target
		final boolean isReturn = (opcode == PicoblazeAssemblerOpcodes.OPCODE_RETURN);

		// fetch the first argument token
		final Token token1 = tokenizer.getToken();

		// if this is a newline, then the instruction must be an unconditional return
		if (token1.getCode() == Token.TOKEN_NEWLINE) {
			if (!isReturn) {
				parser.expected(token1, "condition or label");
			}
			client.instructionJ(location, location, location, opcode, PicoblazeAssemblerOpcodes.CONDITION_NONE, null);
			return;
		}

		// otherwise, it must be an identifer
		if (token1.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(token1, isReturn ? "condition" : "condition or label");
		}
		final String string1 = token1.getIdentifier();

		// fetch a second token
		final Token token2 = tokenizer.getToken();

		// If this is a newline, then we either have a conditional return
		// or an unconditional jump or call.
		if (token2.getCode() == Token.TOKEN_NEWLINE) {
			if (isReturn) {
				handle(location, parser, client, token1, string1, null, null);
			} else {
				handle(location, parser, client, null, null, token1, string1);
			}
			return;
		}

		// return instructions take no more arguments
		if (isReturn) {
			parser.expected(token2, "end of line");
		}

		// we have a conditional jump or call, so expect a comma
		if (token2.getCode() != Token.TOKEN_COMMA) {
			parser.expected(token2, "comma");
		}

		// and another identifier token
		final Token token3 = tokenizer.getToken();
		if (token3.getCode() != Token.TOKEN_IDENTIFIER) {
			parser.expected(token3, "label");
		}

		// ... and the end-of-line
		parser.expectNewline();

		// handle the conditional call or jump
		handle(location, parser, client, token1, string1, token3, token3.getIdentifier());
	}

	private void handle(final Token location, final Parser parser, final IParserClient client, final Token conditionToken, final String conditionString, final Token labelToken, final String labelString) throws LineSyntaxException {

		int condition;
		if (conditionString != null) {
			if (conditionString.equalsIgnoreCase("z")) {
				condition = PicoblazeAssemblerOpcodes.CONDITION_Z;
			} else if (conditionString.equalsIgnoreCase("nz")) {
				condition = PicoblazeAssemblerOpcodes.CONDITION_NZ;
			} else if (conditionString.equalsIgnoreCase("c")) {
				condition = PicoblazeAssemblerOpcodes.CONDITION_C;
			} else if (conditionString.equalsIgnoreCase("nc")) {
				condition = PicoblazeAssemblerOpcodes.CONDITION_NC;
			} else {
				throw new LineSyntaxException(conditionToken, "Invalid condition: " + conditionString, false);
			}
		} else {
			condition = PicoblazeAssemblerOpcodes.CONDITION_NONE;
		}

		Range fullRange = (labelToken != null) ? new Range(location, labelToken) : (conditionToken != null) ? new Range(location, conditionToken) : location;
		Range conditionRange = (conditionToken != null) ? conditionToken : location;
		Range targetRange = (labelToken != null) ? labelToken : location;
		client.instructionJ(fullRange, conditionRange, targetRange, opcode, condition, labelString);
	}

}
