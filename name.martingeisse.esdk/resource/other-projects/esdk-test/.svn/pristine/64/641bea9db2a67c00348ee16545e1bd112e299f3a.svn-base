/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.synthesis.codegen;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 */
public class CodeAssemblerTest {

	/**
	 * the stringWriter
	 */
	private StringWriter stringWriter = new StringWriter();
	
	/**
	 * the printWriter
	 */
	private PrintWriter printWriter = new PrintWriter(stringWriter);
	
	/**
	 * the codeAssembler
	 */
	private CodeAssembler codeAssembler = new CodeAssembler(printWriter);
	
	/**
	 * @param expectedText
	 */
	private void assertText(String expectedText) {
		assertEquals(expectedText, stringWriter.toString());
	}
	
	/**
	 * 
	 */
	@Test
	public void testGetPrintWriter() {
		assertSame(printWriter, codeAssembler.getPrintWriter());
	}
	
	/**
	 * 
	 */
	@Test
	public void testEmpty() {
		assertText("");
	}

	/**
	 * 
	 */
	@Test
	public void testIndentation() {
		assertEquals(0, codeAssembler.getIndentation());
		codeAssembler.setIndentation(5);
		assertEquals(5, codeAssembler.getIndentation());
		codeAssembler.startIndentation();
		assertEquals(6, codeAssembler.getIndentation());
		codeAssembler.setIndentation(5);
		assertEquals(5, codeAssembler.getIndentation());
		codeAssembler.endIndentation();
		assertEquals(4, codeAssembler.getIndentation());
		codeAssembler.endIndentation();
		codeAssembler.printIndentation();
		assertText("\t\t\t");
		codeAssembler.setIndentation(0);
		assertEquals(0, codeAssembler.getIndentation());
	}

	/**
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSetIndentationNegative() {
		codeAssembler.setIndentation(-1);
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testEndIndentationWhenZero() {
		codeAssembler.endIndentation();
	}
	
	/**
	 * 
	 */
	@Test
	public void testPrintMethods() {
		codeAssembler.setIndentation(3);
		assertText("");
		codeAssembler.println("foo");
		assertText("foo\n");
		codeAssembler.println("bar");
		assertText("foo\nbar\n");
		codeAssembler.println();
		assertText("foo\nbar\n\n");
		codeAssembler.print("baz");
		assertText("foo\nbar\n\nbaz");
		codeAssembler.printWholeLine("brx");
		assertText("foo\nbar\n\nbaz\t\t\tbrx\n");
	}

}
