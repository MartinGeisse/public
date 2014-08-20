/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonast;

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
	public void testInitialState() {
		JsonLexerInput input = new JsonLexerInput("foo");
		assertEquals(0, input.getLine());
		assertEquals(0, input.getColumn());
		assertEquals('f', input.getCurrentCharacter());
	}
	
}
