/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.parserbase;

import name.martingeisse.common.javascript.ownjson.parserbase.JsonLexer;
import name.martingeisse.common.javascript.ownjson.parserbase.JsonLexerInput;
import name.martingeisse.common.javascript.ownjson.parserbase.JsonToken;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 */
public class JsonLexerTest {

	/**
	 * 
	 */
	@Test
	public void testTokenProperties() {
		
		JsonLexer lexer = new JsonLexer(new JsonLexerInput("   ,"));
		lexer.readToken("test");
		assertEquals(0, lexer.getTokenStartLine());
		assertEquals(3, lexer.getTokenStartColumn());
		
		lexer = new JsonLexer(new JsonLexerInput("  \n ,"));
		lexer.readToken("test");
		assertEquals(1, lexer.getTokenStartLine());
		assertEquals(1, lexer.getTokenStartColumn());
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testPunctuation() {
		testPunctuationHelper("[]{}:,");
		testPunctuationHelper(" [  ]  {  }  :  ,  ");
		testPunctuationHelper("\n[\n]\n{\n}\n:\n,\n");
	}
	
	private void testPunctuationHelper(String input) {
		JsonLexer lexer = new JsonLexer(new JsonLexerInput(input));
		assertEquals(JsonToken.OPENING_SQUARE_BRACKET, lexer.readToken("test"));
		assertEquals(JsonToken.CLOSING_SQUARE_BRACKET, lexer.readToken("test"));
		assertEquals(JsonToken.OPENING_CURLY_BRACE, lexer.readToken("test"));
		assertEquals(JsonToken.CLOSING_CURLY_BRACE, lexer.readToken("test"));
		assertEquals(JsonToken.COLON, lexer.readToken("test"));
		assertEquals(JsonToken.COMMA, lexer.readToken("test"));
	}

	/**
	 * 
	 */
	@Test
	public void testNumbers() {
		JsonLexer lexer = new JsonLexer(new JsonLexerInput(" 123 45 10.23 1e6 10e6"));
		
		assertEquals(JsonToken.INTEGER, lexer.readToken("test"));
		assertEquals(123, lexer.getTokenIntegerValue());
		
		assertEquals(JsonToken.INTEGER, lexer.readToken("test"));
		assertEquals(45, lexer.getTokenIntegerValue());
		
		assertEquals(JsonToken.FLOAT, lexer.readToken("test"));
		assertEquals(10.23, lexer.getTokenFloatingPointValue(), 0.0);
		
		assertEquals(JsonToken.FLOAT, lexer.readToken("test"));
		assertEquals(1000000.0, lexer.getTokenFloatingPointValue(), 0.0);
		
		assertEquals(JsonToken.FLOAT, lexer.readToken("test"));
		assertEquals(10000000.0, lexer.getTokenFloatingPointValue(), 0.0);
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testKeywords() {
		JsonLexer lexer = new JsonLexer(new JsonLexerInput(" abc def "));
		
		assertEquals(JsonToken.KEYWORD, lexer.readToken("test"));
		assertEquals("abc", lexer.getTokenStringValue());
		
		assertEquals(JsonToken.KEYWORD, lexer.readToken("test"));
		assertEquals("def", lexer.getTokenStringValue());
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testStrings() {
		JsonLexer lexer = new JsonLexer(new JsonLexerInput(" \"abc\" \"def\" "));
		
		assertEquals(JsonToken.STRING, lexer.readToken("test"));
		assertEquals("abc", lexer.getTokenStringValue());
		
		assertEquals(JsonToken.STRING, lexer.readToken("test"));
		assertEquals("def", lexer.getTokenStringValue());
		
	}

}
