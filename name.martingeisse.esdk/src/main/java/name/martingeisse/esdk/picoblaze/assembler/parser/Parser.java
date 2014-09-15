package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;
import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * This class turns a token stream from a {@link Tokenizer}
 * into a series of {@link IParserClient} events.
 */
public class Parser {

	/**
	 * the tokenizer
	 */
	private final Tokenizer tokenizer;

	/**
	 * the errorHandler
	 */
	private final IPicoblazeAssemblerErrorHandler errorHandler;
	
	/**
	 * Constructor.
	 * @param tokenizer the tokenizer that defines the input token stream
	 * @param errorHandler the error handler
	 */
	public Parser(final Tokenizer tokenizer, final IPicoblazeAssemblerErrorHandler errorHandler) {
		this.tokenizer = tokenizer;
		this.errorHandler = errorHandler;
	}

	/**
	 * Parses the input from the tokenizer and feeds parsing events
	 * to the specified parser client.
	 * @param client the parser client
	 * @throws IOException on I/O clients
	 */
	public void parse(final IParserClient client) throws IOException {
		while (true) {
			final Token token1 = tokenizer.getToken();

			// empty lines may be pragma comments
			if (token1.getCode() == Token.TOKEN_NEWLINE) {
				String value = (String)token1.getValue();
				if (value != null) {
					value = value.trim();
					if (value.startsWith("@")) {
						handlePragma(client, token1, value);
					}
				}
				continue;
			}

			// halt on end-of-file
			if (token1.getCode() == Token.TOKEN_EOF) {
				return;
			}

			// constrain syntax errors to a single line if possible
			try {

				// all valid input starts with an identifier
				if (token1.getCode() != Token.TOKEN_IDENTIFIER) {
					expected(token1, "directive, instruction, or label");
				}

				// check for directive or instruction
				final String directiveId = token1.getIdentifier();
				final DirectiveParser directiveParser = DirectiveParserRegistry.instance.get(directiveId);
				if (directiveParser != null) {
					directiveParser.parse(token1, tokenizer, this, client);
					continue;
				}

				// must be a label
				final Token token2 = tokenizer.getToken();
				if (token2.getCode() != Token.TOKEN_COLON) {
					expected(token1, "directive, instruction, or label");
				}

				// place the label and restart as if at beginning of the line
				client.label(new Range(token1, token2), token1, directiveId);
				
			} catch (LineSyntaxException e) {
				e.report(errorHandler);
				if (e.isSkipLine()) {
					tokenizer.skipLine();
				}
			}
			
		}
	}
	
	private void handlePragma(final IParserClient client, Token commentToken, String trimmedComment) {
		int i;
		for (i = 1; i < trimmedComment.length(); i++) {
			char c = trimmedComment.charAt(i);
			if (c >= '0' && c <= '9') {
				continue;
			}
			if (c >= 'a' && c <= 'z') {
				continue;
			}
			if (c >= 'A' && c <= 'Z') {
				continue;
			}
			if (c == '_' || c == '.') {
				continue;
			}
			break;
		}
		String identifier = trimmedComment.substring(1, i);
		if (identifier.length() == 0) {
			errorHandler.handleWarning(commentToken, "pragma comment with empty identifier");
			return;
		}
		String parameter = trimmedComment.substring(i).trim();
		client.pragma(commentToken, identifier, parameter);
	}

	/**
	 * Throws a {@link LineSyntaxException} about an unexpected token.
	 * @param token the token that was unexpected
	 * @param description a description of what was expected
	 * @throws LineSyntaxException always
	 */
	public void expected(Token token, String description) throws LineSyntaxException {
		throw new LineSyntaxException(token, "expected: " + description);
	}
	
	/**
	 * Expects a comma as the next token and throws a syntax error
	 * exception if that is not the case.
	 * @return the parsed token
	 * @throws LineSyntaxException if something else than a comma was found
	 * @throws IOException on I/O clients
	 */
	public Token expectComma() throws LineSyntaxException, IOException {
		final Token token = tokenizer.getToken();
		if (token.getCode() != Token.TOKEN_COMMA) {
			expected(token, "comma");
		}
		return token;
	}

	/**
	 * Expects a newline as the next token and throws a syntax error
	 * exception if that is not the case.
	 * @return the parsed token
	 * @throws LineSyntaxException if something else than a newline was found
	 * @throws IOException on I/O clients
	 */
	public Token expectNewline() throws LineSyntaxException, IOException {
		final Token token = tokenizer.getToken();
		if (token.getCode() != Token.TOKEN_NEWLINE) {
			expected(token, "end of line");
		}
		return token;
	}

	/**
	 * Expects a closing parenthesis as the next token and throws a syntax error
	 * exception if that is not the case.
	 * @return the parsed token
	 * @throws LineSyntaxException if something else than a closing parenthesis was found
	 * @throws IOException on I/O clients
	 */
	public Token expectClosingParenthesis() throws LineSyntaxException, IOException {
		final Token token = tokenizer.getToken();
		if (token.getCode() != Token.TOKEN_RPAREN) {
			expected(token, "closing parenthesis");
		}
		return token;
	}

	/**
	 * Expects the specified identifier as the next token and throws a syntax error
	 * exception if that is not the case.
	 * @param id the expected identifier
	 * @return the parsed token
	 * @throws LineSyntaxException if something else than an identifier was found
	 * @throws IOException on I/O clients
	 */
	public Token expectIdentifier(final String id) throws LineSyntaxException, IOException {
		final Token token = tokenizer.getToken();
		if (token.getCode() != Token.TOKEN_IDENTIFIER || !id.equalsIgnoreCase(token.getIdentifier())) {
			expected(token, "\"" + id + "\"");
		}
		return token;
	}

}
