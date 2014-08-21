/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.javascript.ownjson.parserbase;


/**
 * base class for the JSON parser. This class implements the parsing logic
 * but does not create AST nodes. It can be used as the basis for SAJ-like
 * parsers.
 */
public abstract class AbstractJsonParser {

	/**
	 * the lexer
	 */
	private final JsonLexer lexer;
	
	/**
	 * the state
	 */
	private AbstractJsonParserState state;

	/**
	 * Constructor.
	 * @param lexer the lexical analyzer
	 */
	public AbstractJsonParser(JsonLexer lexer) {
		this.lexer = lexer;
	}

	/**
	 * Parses the input and invokes event handler methods.
	 * 
	 * @param initialState the initial parser state
	 */
	protected final void parse(AbstractJsonParserState initialState) {
		this.state = initialState;
		parse(false);
		this.state = null;
		JsonToken suffixToken = lexer.readToken("end of input");
		if (suffixToken != JsonToken.EOF) {
			throw newSyntaxException("end of input", suffixToken.getDescription());
		}
	}

	/**
	 * Parses the input and invokes event handler methods.
	 * 
	 * @return true for values, false for end-of-array
	 */
	private final boolean parse(boolean acceptEndOfArray) {
		String expected = (acceptEndOfArray ? "JSON value or end of array" : "JSON value");
		JsonToken token = lexer.readToken(expected);
		int line = lexer.getTokenLine();
		int column = lexer.getTokenColumn();
		switch (token) {
		
		case KEYWORD: {
			String keyword = lexer.getTokenStringValue();
			if (keyword.equals("null")) {
				state = state.handleNullValue(line, column);
			} else if (keyword.equals("true")) {
				state = state.handleBooleanValue(line, column, true);
			} else if (keyword.equals("false")) {
				state = state.handleBooleanValue(line, column, false);
			} else {
				throw newSyntaxException("invalid keyword: " + keyword);
			}
			break;
		}
			
		case INTEGER: {
			state = state.handleIntegerValue(line, column, lexer.getTokenIntegerValue());
			break;
		}
			
		case FLOAT: {
			state = state.handleFloatingPointValue(line, column, lexer.getTokenFloatingPointValue());
			break;
		}
			
		case STRING: {
			state = state.handleStringValue(line, column, lexer.getTokenStringValue());
			break;
		}
			
		case EOF:
			throw newSyntaxException(expected, "end-of-input");
			
		case OPENING_SQUARE_BRACKET:
			state = state.handleBeginArray(line, column);
			if (parse(true)) {
				while (true) {
					JsonToken commaToken = lexer.readToken("comma or end of array");
					if (commaToken == JsonToken.CLOSING_SQUARE_BRACKET) {
						break;
					} else if (commaToken != JsonToken.COMMA) {
						throw newSyntaxException("comma or end of array", commaToken.getDescription());
					}
					parse(false);
				}
			}
			state = state.handleEndArray(line, column);
			break;
			
		case OPENING_CURLY_BRACE: {
			state = state.handleBeginObject(line, column);
			if (parseObjectProperty(true)) {
				while (true) {
					JsonToken commaToken = lexer.readToken("comma or end of object");
					if (commaToken == JsonToken.CLOSING_CURLY_BRACE) {
						break;
					} else if (commaToken != JsonToken.COMMA) {
						throw newSyntaxException("comma or end of object", commaToken.getDescription());
					}
					parseObjectProperty(false);
				}
			}
			state = state.handleEndObject(line, column);
			break;
		}
			
		case CLOSING_SQUARE_BRACKET:
			if (!acceptEndOfArray) {
				throw newSyntaxException(expected, "']'");
			} else {
				return false;
			}
			
		case CLOSING_CURLY_BRACE:
			throw newSyntaxException(expected, "'}'");
			
		case COMMA:
			throw newSyntaxException(expected, "','");
			
		case COLON:
			throw newSyntaxException(expected, "':'");
			
		}
		return true;
	}
	
	/**
	 * 
	 */
	private boolean parseObjectProperty(boolean first) {
		
		// parse name (also handling empty objects)
		int line = lexer.getTokenLine();
		int column = lexer.getTokenColumn();
		String nameExpected = (first ? "property name or end of object" : "property name");
		JsonToken nameToken = lexer.readToken(nameExpected);
		if (first && nameToken == JsonToken.CLOSING_CURLY_BRACE) {
			return false;
		} else if (nameToken != JsonToken.STRING) {
			throw newSyntaxException(nameExpected, nameToken.getDescription());
		}
		state = state.handleObjectPropertyName(line, column, lexer.getTokenStringValue());
		
		// parse colon
		JsonToken colonToken = lexer.readToken("':'");
		if (colonToken != JsonToken.COLON) {
			throw newSyntaxException("':'", colonToken.getDescription());
		}
		
		// parse value
		parse(false);
		return true;

	}
	
	/**
	 * 
	 */
	private JsonSyntaxException newSyntaxException(String message) {
		return new JsonSyntaxException(lexer.getTokenLine(), lexer.getTokenColumn(), message);
	}
	
	/**
	 * 
	 */
	private JsonSyntaxException newSyntaxException(String expected, String actual) {
		return new JsonSyntaxException(lexer.getTokenLine(), lexer.getTokenColumn(), expected, actual);
	}

}
