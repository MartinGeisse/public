/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * This class splits JSON into tokens.
 */
public final class JsonLexer {

	/**
	 * the input
	 */
	private final JsonLexerInput input;
	
	/**
	 * the token
	 */
	private JsonToken token;
	
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
	private String tokenStringValue;
	
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
	 * @param input the input to tokenize
	 */
	public JsonLexer(JsonLexerInput input) {
		this.input = input;
	}
	
	/**
	 * Getter method for the token.
	 * @return the token
	 */
	public JsonToken getToken() {
		return token;
	}
	
	/**
	 * Getter method for the tokenLine.
	 * @return the tokenLine
	 */
	public int getTokenLine() {
		return tokenLine;
	}
	
	/**
	 * Getter method for the tokenColumn.
	 * @return the tokenColumn
	 */
	public int getTokenColumn() {
		return tokenColumn;
	}
	
	/**
	 * Getter method for the tokenStringValue.
	 * @return the tokenStringValue
	 */
	public String getTokenStringValue() {
		return tokenStringValue;
	}
	
	/**
	 * Getter method for the tokenIntegerValue.
	 * @return the tokenIntegerValue
	 */
	public long getTokenIntegerValue() {
		return tokenIntegerValue;
	}
	
	/**
	 * Getter method for the tokenFloatingPointValue.
	 * @return the tokenFloatingPointValue
	 */
	public double getTokenFloatingPointValue() {
		return tokenFloatingPointValue;
	}
	
	/**
	 * Reads the next token.
	 * @param expected a description of the expected input, for error messages
	 */
	public void readToken(String expected) {
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
			handlePunctuation(JsonToken.OPENING_SQUARE_BRACKET);
		} else if (c == ']') {
			handlePunctuation(JsonToken.CLOSING_SQUARE_BRACKET);
		} else if (c == '{') {
			handlePunctuation(JsonToken.OPENING_CURLY_BRACE);
		} else if (c == '}') {
			handlePunctuation(JsonToken.CLOSING_CURLY_BRACE);
		} else if (c == ',') {
			handlePunctuation(JsonToken.COMMA);
		} else if (c == ':') {
			handlePunctuation(JsonToken.COLON);
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
			token = JsonToken.EOF;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 */
	private void handlePunctuation(JsonToken token) {
		this.token = token;
		this.tokenLine = input.getLine();
		this.tokenColumn = input.getColumn();
		input.step();
	}
	
	/**
	 * 
	 */
	private void handleNumber() {
		int startPosition = inputPosition, startColumn = column;
		boolean isFloat = false;
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c == '.' || c == 'e') {
				isFloat = true;
			} else if (c < '0' || c > '9') {
				break;
			}
			column++;
			inputPosition++;
		}
		tokenLine = line;
		tokenColumn = startColumn;
		String text = input.subSequence(startPosition, inputPosition).toString();
		if (isFloat) {
			token = JsonToken.FLOAT;
			tokenFloatingPointValue = Double.parseDouble(text);
		} else {
			token = JsonToken.INTEGER;
			tokenFloatingPointValue = Long.parseLong(text);
		}
	}

	/**
	 * 
	 */
	private void handleKeyword() {
		int startPosition = inputPosition, startColumn = column;
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c < 'a' || c > 'z') {
				break;
			}
			inputPosition++;
		}
		token = JsonToken.KEYWORD;
		tokenLine = line;
		tokenColumn = startColumn;
		tokenStringValue = input.subSequence(startPosition, inputPosition).toString();
	}

	/**
	 * 
	 */
	private void handleString() {
		int startPosition = inputPosition, startColumn = column;
		
		// skip the initial quote character
		this.column++;
		this.inputPosition++;
		
		
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c < 'a' || c > 'z') {
				break;
			}
			inputPosition++;
		}
		token = JsonToken.KEYWORD;
		tokenLine = line;
		tokenColumn = startColumn;
		tokenStringValue = input.subSequence(startPosition, inputPosition).toString();
	}

}
