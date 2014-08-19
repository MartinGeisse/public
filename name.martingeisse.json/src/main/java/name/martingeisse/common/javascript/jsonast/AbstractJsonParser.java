/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.jsonast;


/**
 * base class for the JSON parser. This class implements the parsing logic
 * but does not create AST nodes. It can be used as the basis for SAJ-like
 * parsers.
 */
public abstract class AbstractJsonParser {

	/**
	 * the input
	 */
	private final CharSequence input;
	
	/**
	 * the inputLength
	 */
	private final int inputLength;
	
	/**
	 * the inputPosition
	 */
	private int inputPosition;
	
	/**
	 * the line
	 */
	private int line;
	
	/**
	 * the column
	 */
	private int column;
	
	/**
	 * the token
	 */
	private Token token;
	
	/**
	 * the tokenLine
	 */
	private int tokenLine;
	
	/**
	 * the tokenColumn
	 */
	private int tokenColumn;
	
	/**
	 * the tokenStringValue
	 */
	private StringBuilder tokenStringValue;
	
	/**
	 * the tokenIntegerValue
	 */
	private long tokenIntegerValue;
	
	/**
	 * the tokenFloatingPointValue
	 */
	private double tokenFloatingPointValue;
	
	/**
	 * Constructor.
	 */
	public AbstractJsonParser(CharSequence input) {
		this.input = input;
		this.inputLength = input.length();
		this.inputPosition = 0;
		this.line = 0;
		this.column = 0;
		this.token = null;
	}
	
	/**
	 * Parses the input and invokes event handler methods.
	 */
	protected final void parse() {
		
	}
	
	/**
	 * 
	 */
	private void readToken(String expected) {
		skipSpaces();
		if (checkEof()) {
			return;
		}
		char c = input.charAt(inputPosition);
		if ((c >= '0' && c <= '9') || c == '.') {
			handleNumber();
		} else if (c >= 'a' && c <= 'z') {
			handleKeyword();
		} else if (c == '"') {
			handleString();
		} else if (c == '[') {
			handlePunctuation(Token.OPENING_SQUARE_BRACKET);
		} else if (c == ']') {
			handlePunctuation(Token.CLOSING_SQUARE_BRACKET);
		} else if (c == '{') {
			handlePunctuation(Token.OPENING_CURLY_BRACE);
		} else if (c == '}') {
			handlePunctuation(Token.CLOSING_CURLY_BRACE);
		} else if (c == ',') {
			handlePunctuation(Token.COMMA);
		} else if (c == ':') {
			handlePunctuation(Token.COLON);
		} else {
			throw new JsonSyntaxException(line, column, expected, Character.toString(c));
		}
	}
	
	/**
	 * 
	 */
	private void skipSpaces() {
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c > 32) {
				break;
			}
			if (c == '\n' || c == '\r') {
				line++;
				column = 0;
			} else {
				column++;
			}
			inputPosition++;
		}
	}
	
	/**
	 * 
	 */
	private boolean checkEof() {
		if (inputPosition == inputLength) {
			token = Token.EOF;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 */
	private void handlePunctuation(Token token) {
		storeToken(token);
		this.column++;
		this.inputPosition++;
	}
	
	/**
	 * 
	 */
	private void handleNumber() {
		// TODO
	}

	/**
	 * 
	 */
	private void handleKeyword() {
		// TODO
	}

	/**
	 * 
	 */
	private void handleString() {
		// TODO
	}

	/**
	 * 
	 */
	private void storeToken(Token token) {
		this.token = token;
		this.tokenLine = line;
		this.tokenColumn = column;
	}
	
	/**
	 *
	 */
	private static enum Token {
		KEYWORD, INTEGER, FLOAT, STRING,
		OPENING_SQUARE_BRACKET, CLOSING_SQUARE_BRACKET, OPENING_CURLY_BRACE, CLOSING_CURLY_BRACE,
		COMMA, COLON, EOF
	}
	
}
