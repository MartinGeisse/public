package name.martingeisse.esdk.picoblaze.assembler.parser;

import java.io.IOException;
import java.io.Reader;

import name.martingeisse.esdk.picoblaze.assembler.IPicoblazeAssemblerErrorHandler;

/**
 * This class splits the character stream from a {@link Reader}
 * into {@link Token}s.
 */
public class Tokenizer {

	/* Implementation note: This tokenizer is based on a 1-character lookahead
	 * scheme. It stores the position of the lookahead character as well as a
	 * "marked" position that indicates the beginning of the current token while
	 * scanning the remainder of that token.
	 */
	
	/**
	 * the in
	 */
	private final Reader in;

	/**
	 * the lookaheadOffset
	 */
	private int lookaheadOffset;
	
	/**
	 * the lookaheadLine
	 */
	private int lookaheadLine;
	
	/**
	 * the lookaheadColumn
	 */
	private int lookaheadColumn;
	
	/**
	 * the markedOffset
	 */
	private int markedOffset;
	
	/**
	 * the markedLine
	 */
	private int markedLine;
	
	/**
	 * the markedColumn
	 */
	private int markedColumn;

	/**
	 * the lookaheadCharacter
	 */
	private int lookaheadCharacter;
	
	/**
	 * the errorHandler
	 */
	private final IPicoblazeAssemblerErrorHandler errorHandler;
	
	/**
	 * Constructor.
	 * @param in the reader to read from
	 * @param errorHandler the error handler
	 * @throws IOException on I/O errors
	 */
	public Tokenizer(final Reader in, final IPicoblazeAssemblerErrorHandler errorHandler) throws IOException {
		this.in = in;
		this.lookaheadOffset = 0;
		this.lookaheadLine = 0;
		this.lookaheadColumn = 0;
		this.lookaheadCharacter = in.read();
		this.markedOffset = 0;
		this.markedLine = 0;
		this.markedColumn = 0;
		this.errorHandler = errorHandler;
	}

	/**
	 * Fetches the next character
	 * @throws IOException on I/O errors
	 */
	private void fetch() throws IOException {
		
		// check for EOF
		if (lookaheadCharacter < 0) {
			return;
		}
		
		// check for EOL
		if (lookaheadCharacter == '\n') {
			lookaheadLine++;
			lookaheadColumn = 0;
			lookaheadOffset++;
		} else {
			lookaheadColumn++;
			lookaheadOffset++;
		}
		
		// actually read the next character
		lookaheadCharacter = in.read();

	}

	/**
	 * Marks nextChar as the beginning of some interesting character
	 * sequence.
	 */
	private void mark() {
		markedOffset = lookaheadOffset;
		markedLine = lookaheadLine;
		markedColumn = lookaheadColumn;
	}

	/**
	 * Creates a token whose starting position is set by the latest call to mark()
	 * and which extends up to but excluding the current lookahead character.
	 */
	private Token createToken(final int code, final Object value) {
		return new Token(code, value, markedOffset, markedLine, markedColumn, lookaheadOffset, lookaheadLine, lookaheadColumn);
	}

	/**
	 * Computes the value of a hex digit character.
	 * @param c the character to interpret
	 * @return the numeric value of a hex digit, or -1 if not a digit.
	 */
	public static int hexDigitValue(final int c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		}
		if (c >= 'a' && c <= 'f') {
			return c - 'a' + 10;
		}
		if (c >= 'A' && c <= 'F') {
			return c - 'A' + 10;
		}
		return -1;
	}

	/**
	 * Throws an exception about a syntax error that was detected
	 * @param message the error message
	 */
	private void lexicalError(final String message) {
		final Token location = new Token(Token.TOKEN_INVALID, null, lookaheadOffset, lookaheadLine, lookaheadColumn, lookaheadOffset + 1, lookaheadLine, lookaheadColumn + 1);
		errorHandler.handleError(location, message);
	}

	/**
	 * Fetches characters as long as the lookahead character is a
	 * non-newline whitespace character. The current lookahead character
	 * at the time this method is invoked is checked too, so if it isn't
	 * a non-newline whitespace character, then no characters are skipped
	 * at all.
	 * @throws IOException on I/O errors
	 */
	private void skipWhitespaceExceptNewline() throws IOException {
		while (lookaheadCharacter >= 0 && Character.isWhitespace(lookaheadCharacter) && lookaheadCharacter != '\n') {
			fetch();
		}
	}

	/**
	 * Fetches characters as long as the lookahead character is any
	 * character except	a newline character. The current lookahead character
	 * at the time this method is invoked is checked too, so if it is
	 * a newline character, then no characters are skipped at all.
	 * @param capture whether the skipped characters shall be captured
	 * @return the captured characters, or null if the capture argument was false
	 * @throws IOException on I/O errors
	 */
	private String skipCharactersExceptNewline(boolean capture) throws IOException {
		StringBuilder captured = (capture ? new StringBuilder() : null);
		while (lookaheadCharacter >= 0 && lookaheadCharacter != '\n') {
			if (capture) {
				captured.append((char)lookaheadCharacter);
			}
			fetch();
		}
		return (capture ? captured.toString() : null);
	}
	
	/**
	 * Reads the next token
	 * @return the token
	 * @throws IOException on I/O errors
	 */
	public Token getToken() throws IOException {
		
		// whitespace is insignificant, but newlines are returned
		skipWhitespaceExceptNewline();

		// check for comment
		if (lookaheadCharacter == ';') {

			// mark position as the starting point of the comment token
			mark();

			// skip the semicolon
			fetch();
			
			// skip comment characters
			String value = skipCharactersExceptNewline(true).trim();
			
			// create the token
			Token token = createToken(Token.TOKEN_NEWLINE, value);
			
			// skip the newline character
			fetch();
			
			// done
			return token;
			
		}

		// mark the beginning of a token
		mark();

		// handle EOF tokens
		if (lookaheadCharacter == -1) {
			return createToken(Token.TOKEN_EOF, null);
		}

		// handle end of line
		if (lookaheadCharacter == '\n') {
			fetch();
			return createToken(Token.TOKEN_NEWLINE, null);
		}

		// handle special characters
		if (lookaheadCharacter == ',') {
			fetch();
			return createToken(Token.TOKEN_COMMA, null);
		}

		if (lookaheadCharacter == ':') {
			fetch();
			return createToken(Token.TOKEN_COLON, null);
		}

		if (lookaheadCharacter == '(') {
			fetch();
			return createToken(Token.TOKEN_LPAREN, null);
		}

		if (lookaheadCharacter == ')') {
			fetch();
			return createToken(Token.TOKEN_RPAREN, null);
		}

		// collect identifier/number characters
		String s = "";
		while (Character.isJavaIdentifierPart(lookaheadCharacter)) {
			s += (char)lookaheadCharacter;
			fetch();
		}

		// need at least one character
		if (s.length() == 0) {
			lexicalError("invaid character: " + (char)lookaheadCharacter + " (" + lookaheadCharacter + ")");
			
			// Skip one character, then return the next token. Do NOT skip the remainder of the line here!
			// By doing so, the high-level parser would not notice the syntax error and choke on the first
			// token of the next line.
			fetch();
			return getToken();
		}

		// check for a number
		if (s.length() == 2) {
			final int digit1 = hexDigitValue(s.charAt(0));
			final int digit2 = hexDigitValue(s.charAt(1));
			if (digit1 != -1 && digit2 != -1) {
				return createToken(Token.TOKEN_NUMBER, (digit1 << 4) + digit2);
			}
		}

		// must be an identifier
		return createToken(Token.TOKEN_IDENTIFIER, s);
	}

	/**
	 * Skips the remainder of the current line, including the newline character.
	 * @throws IOException on I/O errors
	 */
	public void skipLine() throws IOException {
		skipCharactersExceptNewline(false);
		fetch();
	}
	
}
