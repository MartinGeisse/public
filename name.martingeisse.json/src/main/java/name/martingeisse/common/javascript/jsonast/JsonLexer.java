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
	 * the tokenLine
	 */
	private int tokenLine;
	
	/**
	 * the tokenColumn
	 */
	private int tokenColumn;
	
	/**
	 * the token
	 */
	private JsonToken token;
	
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
	 * Reads the next token, and returns it just like {@link #getToken()} does.
	 * 
	 * @param expected a description of the expected input, for error messages
	 * @return the token just read
	 */
	public JsonToken readToken(String expected) {
		try {
			input.skipSpaces();
			int c = input.getCurrentCharacter();
			if (c < 0) {
				token = null;
				return token;
			}
			tokenLine = input.getLine();
			tokenColumn = input.getColumn();
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
				throw new JsonSyntaxException(input.getLine(), input.getColumn(), expected, Character.toString((char)c));
			}
			return token;
		} catch (JsonLexerInputException e) {
			throw new JsonSyntaxException(input.getLine(), input.getColumn(), e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	private void handleNumber() {
		input.readNumber();
		if (input.isFloatingPoint()) {
			token = JsonToken.FLOAT;
			tokenFloatingPointValue = Double.parseDouble(input.getSegment().toString());
		} else {
			token = JsonToken.INTEGER;
			tokenFloatingPointValue = Long.parseLong(input.getSegment().toString());
		}
	}

	/**
	 * 
	 */
	private void handleKeyword() {
		input.readKeyword();
		token = JsonToken.KEYWORD;
		tokenStringValue = input.getSegment().toString();
	}

	/**
	 * 
	 */
	private void handleString() {
		input.readString();
		token = JsonToken.STRING;
		tokenStringValue = input.getSegment().toString();
	}

	/**
	 * 
	 */
	private void handlePunctuation(JsonToken token) {
		this.token = token;
		input.step();
	}
	
}
