/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.ownjson.parserbase;

import name.martingeisse.common.javascript.ownjson.parserbase.JsonLexerInput;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 */
public class JsonLexerInputTest {

	/**
	 * 
	 */
	@Test
	public void testEmptyStep() {
		JsonLexerInput input = new JsonLexerInput("");
		assertEquals(0, input.getLine());
		assertEquals(0, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
		input.step();
		assertEquals(0, input.getLine());
		assertEquals(0, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
	}
	
	/**
	 * 
	 */
	@Test
	public void testSingleLineStep() {
		JsonLexerInput input = new JsonLexerInput("abc");
		assertEquals(0, input.getLine());
		assertEquals(0, input.getColumn());
		assertEquals('a', input.getCurrentCharacter());
		input.step();
		assertEquals(0, input.getLine());
		assertEquals(1, input.getColumn());
		assertEquals('b', input.getCurrentCharacter());
		input.step();
		assertEquals(0, input.getLine());
		assertEquals(2, input.getColumn());
		assertEquals('c', input.getCurrentCharacter());
		input.step();
		assertEquals(0, input.getLine());
		assertEquals(3, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
		input.step();
		assertEquals(0, input.getLine());
		assertEquals(3, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
	}

	/**
	 * 
	 */
	@Test
	public void testMultiLineStep() {
		JsonLexerInput input = new JsonLexerInput("ab\ncd");
		assertEquals(0, input.getLine());
		assertEquals(0, input.getColumn());
		assertEquals('a', input.getCurrentCharacter());
		input.step();
		assertEquals(0, input.getLine());
		assertEquals(1, input.getColumn());
		assertEquals('b', input.getCurrentCharacter());
		input.step();
		assertEquals(0, input.getLine());
		assertEquals(2, input.getColumn());
		assertEquals('\n', input.getCurrentCharacter());
		input.step();
		assertEquals(1, input.getLine());
		assertEquals(0, input.getColumn());
		assertEquals('c', input.getCurrentCharacter());
		input.step();
		assertEquals(1, input.getLine());
		assertEquals(1, input.getColumn());
		assertEquals('d', input.getCurrentCharacter());
		input.step();
		assertEquals(1, input.getLine());
		assertEquals(2, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
		input.step();
		assertEquals(1, input.getLine());
		assertEquals(2, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
	}

	/**
	 * 
	 */
	@Test
	public void testSkipMixedSpaces() {
		testSkipSpacesHelper(' ', '\t', '\n', '\r', 2, 0, 4, 0, 6, 0);
		testSkipSpacesHelper('\t', '\n', '\r', ' ', 2, 1, 4, 1, 6, 1);
		testSkipSpacesHelper('\n', '\r', ' ', '\t', 2, 2, 4, 2, 6, 2);
		testSkipSpacesHelper('\r', ' ', '\t', '\n', 2, 0, 4, 0, 6, 0);

	}
	
	private void testSkipSpacesHelper(char s1, char s2, char s3, char s4, int line1, int column1, int line2, int column2, int line3, int column3) {
		JsonLexerInput input = new JsonLexerInput("" + s1 + s2 + s3 + s4 + "ab" + s1 + s2 + s3 + s4 + "cd" + s1 + s2 + s3 + s4);
		
		// skip to the a
		input.skipSpaces();
		assertEquals(line1, input.getLine());
		assertEquals(column1, input.getColumn());
		assertEquals('a', input.getCurrentCharacter());
		
		// skipping again has no effect
		input.skipSpaces();
		assertEquals(line1, input.getLine());
		assertEquals(column1, input.getColumn());
		assertEquals('a', input.getCurrentCharacter());
		
		// step and skip -- no spaces, still at b
		input.step();
		input.skipSpaces();
		assertEquals(line1, input.getLine());
		assertEquals(column1 + 1, input.getColumn());
		assertEquals('b', input.getCurrentCharacter());
		
		// step and skip to c
		input.step();
		input.skipSpaces();
		assertEquals(line2, input.getLine());
		assertEquals(column2, input.getColumn());
		assertEquals('c', input.getCurrentCharacter());
		
		// skipping again has no effect -- still at c
		input.skipSpaces();
		assertEquals(line2, input.getLine());
		assertEquals(column2, input.getColumn());
		assertEquals('c', input.getCurrentCharacter());
		
		// step and skip -- no spaces, still at d
		input.step();
		input.skipSpaces();
		assertEquals(line2, input.getLine());
		assertEquals(column2 + 1, input.getColumn());
		assertEquals('d', input.getCurrentCharacter());

		// step and skip to EOF
		input.step();
		input.skipSpaces();
		assertEquals(line3, input.getLine());
		assertEquals(column3, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());

		// skipping again has no effect -- still at EOF
		input.skipSpaces();
		assertEquals(line3, input.getLine());
		assertEquals(column3, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
		
		// even stepping and skipping has no effect -- still at EOF
		input.step();
		input.skipSpaces();
		assertEquals(line3, input.getLine());
		assertEquals(column3, input.getColumn());
		assertEquals(-1, input.getCurrentCharacter());
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testReadNumber() {
		JsonLexerInput input = new JsonLexerInput("123a456");
		input.readNumber();
		assertEquals(0, input.getLine());
		assertEquals(3, input.getColumn());
		assertEquals("123", input.getSegment().toString());
		assertEquals('a', input.getCurrentCharacter());
		input.step();
		input.readNumber();
		assertEquals(0, input.getLine());
		assertEquals(7, input.getColumn());
		assertEquals("456", input.getSegment().toString());
		assertEquals(-1, input.getCurrentCharacter());
	}

	/**
	 * 
	 */
	@Test
	public void testReadKeyword() {
		JsonLexerInput input = new JsonLexerInput("abc1def");
		input.readKeyword();
		assertEquals(0, input.getLine());
		assertEquals(3, input.getColumn());
		assertEquals("abc", input.getSegment().toString());
		assertEquals('1', input.getCurrentCharacter());
		input.step();
		input.readKeyword();
		assertEquals(0, input.getLine());
		assertEquals(7, input.getColumn());
		assertEquals("def", input.getSegment().toString());
		assertEquals(-1, input.getCurrentCharacter());
	}

	/**
	 * 
	 */
	@Test
	public void testReadString() {
		JsonLexerInput input = new JsonLexerInput("\"abc\"d\"efg\"");
		input.readString();
		assertEquals(0, input.getLine());
		assertEquals(5, input.getColumn());
		assertEquals("abc", input.getSegment().toString());
		assertEquals('d', input.getCurrentCharacter());
		input.step();
		input.readString();
		assertEquals(0, input.getLine());
		assertEquals(11, input.getColumn());
		assertEquals("efg", input.getSegment().toString());
		assertEquals(-1, input.getCurrentCharacter());
	}
	
	/**
	 * 
	 */
	@Test
	public void testStringEscapeSequences() {
		JsonLexerInput input = new JsonLexerInput("\".\\\\.\\\".\\/.\\b.\\f.\\n.\\r.\\t.\\u1234.\\u5678.\"");
		input.readString();
		assertEquals(".\\.\"./.\b.\f.\n.\r.\t.\u1234.\u5678.", input.getSegment().toString());
	}

}
