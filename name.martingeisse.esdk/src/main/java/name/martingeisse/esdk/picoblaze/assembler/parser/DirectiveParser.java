package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

/**
 * Abstract base class for sub-parsers that can handle a single
 * assembler directive.
 */
public abstract class DirectiveParser {

	/**
	 * Runs this sub-parser. Consumes as many tokens as needed and
	 * generates appropriate parser client events.
	 * 
	 * @param location the location of the whole parsing unit, ususally used
	 * by the parser client for higher-level error messages
	 * @param tokenizer the tokenizer that providers further tokens
	 * @param parser the parent parser
	 * @param client the client that receives parser events
	 * @throws LineSyntaxException on syntax errors
	 * @throws IOException on I/O errors
	 */
	public abstract void parse(Token location, Tokenizer tokenizer, Parser parser, IParserClient client) throws LineSyntaxException, IOException;

	/**
	 * Reports an error about an unexpected token
	 * @param description a description of what was expected
	 * @param actual the actual token that was found
	 */
	protected final void expected(String description, Token actual) throws LineSyntaxException {
		throw new LineSyntaxException(actual, "expected: " + description);
	}
	
}
