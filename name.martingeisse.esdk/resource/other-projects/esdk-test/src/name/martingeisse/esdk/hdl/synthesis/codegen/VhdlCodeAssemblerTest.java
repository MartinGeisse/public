/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.synthesis.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;

import name.martingeisse.esdk.hdl.PortDirection;

import org.junit.Test;

/**
 * 
 */
public class VhdlCodeAssemblerTest {

	/**
	 * the BOILERPLATE_TEXT
	 */
	private static String BOILERPLATE_TEXT = "library ieee;\nuse ieee.std_logic_1164.all;\n\n";

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
	private VhdlCodeAssembler codeAssembler = new VhdlCodeAssembler(printWriter);

	/**
	 * @param expectedText
	 */
	private void assertBodyText(String expectedBodyText) {
		String text = stringWriter.toString();
		assertTrue(text.startsWith(BOILERPLATE_TEXT));
		assertEquals(expectedBodyText, text.substring(BOILERPLATE_TEXT.length()));
	}

	/**
	 * 
	 */
	@Test
	public void testEmpty() {
		assertBodyText("");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintLibraryDeclaration() {
		codeAssembler.printLibraryDeclaration("foo");
		assertBodyText("library foo;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintUseDeclaration() {
		codeAssembler.printUseDeclaration("bar.xyz");
		assertBodyText("use bar.xyz;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testEmptyEntity() {
		codeAssembler.beginEntity("foo");
		codeAssembler.endEntity();
		assertBodyText("entity foo is\nend foo;\n");
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testStaleEndEntity() {
		codeAssembler.endEntity();
	}

	/**
	 * 
	 */
	@Test
	public void testEmptyPorts() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginPorts();
		codeAssembler.endPorts();
		codeAssembler.endEntity();
		assertBodyText("entity foo is\n\tport (\n\t);\nend foo;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testOneScalarPort() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginPorts();
		codeAssembler.printScalarPortDeclaration("bar", PortDirection.IN);
		codeAssembler.endPorts();
		codeAssembler.endEntity();
		assertBodyText("entity foo is\n\tport (\n\t\tbar: in std_logic\n\t);\nend foo;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testTwoScalarPorts() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginPorts();
		codeAssembler.printScalarPortDeclaration("bar", PortDirection.OUT);
		codeAssembler.printScalarPortDeclaration("baz", PortDirection.IN);
		codeAssembler.endPorts();
		codeAssembler.endEntity();
		assertBodyText("entity foo is\n" + "\tport (\n" + "\t\tbar: out std_logic;\n" + "\t\tbaz: in std_logic\n" + "\t);\n" + "end foo;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testOneVectorPort() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginPorts();
		codeAssembler.printVectorPortDeclaration("bar", PortDirection.IN, 5, 3);
		codeAssembler.endPorts();
		codeAssembler.endEntity();
		assertBodyText("entity foo is\n\tport (\n\t\tbar: in std_logic_vector(5 downto 3)\n\t);\nend foo;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testTwoVectorPorts() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginPorts();
		codeAssembler.printVectorPortDeclaration("bar", PortDirection.OUT, 6, 3);
		codeAssembler.printVectorPortDeclaration("baz", PortDirection.IN, 5, 0);
		codeAssembler.endPorts();
		codeAssembler.endEntity();
		assertBodyText("entity foo is\n" + "\tport (\n" + "\t\tbar: out std_logic_vector(6 downto 3);\n" + "\t\tbaz: in std_logic_vector(5 downto 0)\n" + "\t);\n" + "end foo;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testVectorPortWithOneElement() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginPorts();
		codeAssembler.printVectorPortDeclaration("bar", PortDirection.IN, 2, 2);
		codeAssembler.endPorts();
		codeAssembler.endEntity();
		assertBodyText("entity foo is\n\tport (\n\t\tbar: in std_logic_vector(2 downto 2)\n\t);\nend foo;\n");
	}

	/**
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testVectorPortWithZeroElements() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginPorts();
		codeAssembler.printVectorPortDeclaration("bar", PortDirection.IN, 1, 2);
	}

	/**
	 * 
	 */
	@Test
	public void testEmptyArchitecture() {
		codeAssembler.beginArchitecture("foo", "bar");
		codeAssembler.beginArchitectureBody();
		codeAssembler.endArchitecture();
		assertBodyText("architecture bar of foo is\nbegin\nend bar;\n");
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testStaleBeginArchitectureBody() {
		codeAssembler.beginArchitectureBody();
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testStaleEndArchitecture() {
		codeAssembler.endArchitecture();
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testBeginEntityWithinEntity() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginEntity("fup");
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testBeginEntityWithinArchitecture() {
		codeAssembler.beginArchitecture("foo", "fup");
		codeAssembler.beginEntity("bar");
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testBeginArchitectureWithinEntity() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginArchitecture("bar", "baz");
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testBeginArchitectureWithinArchitecture() {
		codeAssembler.beginArchitecture("foo", "fup");
		codeAssembler.beginArchitecture("bar", "baz");
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testBeginArchitectureBodyWithinEntity() {
		codeAssembler.beginEntity("foo");
		codeAssembler.beginArchitectureBody();
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testEndArchitectureWithinEntity() {
		codeAssembler.beginEntity("foo");
		codeAssembler.endArchitecture();
	}

	/**
	 * 
	 */
	@Test(expected = IllegalStateException.class)
	public void testEndEntityWithinArchitecture() {
		codeAssembler.beginArchitecture("bar", "baz");
		codeAssembler.endEntity();
	}

	/**
	 * 
	 */
	@Test
	public void testArchitectureWithOneScalarSignal() {
		codeAssembler.beginArchitecture("foo", "bar");
		codeAssembler.printScalarSignalDeclaration("xyz");
		codeAssembler.beginArchitectureBody();
		codeAssembler.endArchitecture();
		assertBodyText("architecture bar of foo is\n\tsignal xyz: std_logic;\nbegin\nend bar;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testArchitectureWithOneVectorSignal() {
		codeAssembler.beginArchitecture("foo", "bar");
		codeAssembler.printVectorSignalDeclaration("abc", 5, 3);
		codeAssembler.beginArchitectureBody();
		codeAssembler.endArchitecture();
		assertBodyText("architecture bar of foo is\n\tsignal abc: std_logic_vector(5 downto 3);\nbegin\nend bar;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testArchitectureWithOneSingleElementVectorSignal() {
		codeAssembler.beginArchitecture("foo", "bar");
		codeAssembler.printVectorSignalDeclaration("abc", 2, 2);
		codeAssembler.beginArchitectureBody();
		codeAssembler.endArchitecture();
		assertBodyText("architecture bar of foo is\n\tsignal abc: std_logic_vector(2 downto 2);\nbegin\nend bar;\n");
	}


	/**
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testArchitectureWithOneZeroElementVectorSignal() {
		codeAssembler.beginArchitecture("foo", "bar");
		codeAssembler.printVectorSignalDeclaration("abc", 1, 2);
	}

	/**
	 * 
	 */
	@Test
	public void testArchitectureWithMultipleSignals() {
		codeAssembler.beginArchitecture("foo", "bar");
		codeAssembler.printScalarSignalDeclaration("abc");
		codeAssembler.printVectorSignalDeclaration("def", 5, 3);
		codeAssembler.printScalarSignalDeclaration("ghi");
		codeAssembler.beginArchitectureBody();
		codeAssembler.endArchitecture();
		assertBodyText("architecture bar of foo is\n\tsignal abc: std_logic;\n\tsignal def: std_logic_vector(5 downto 3);\n\tsignal ghi: std_logic;\nbegin\nend bar;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintScalarType() {
		codeAssembler.printScalarType();
		assertBodyText("std_logic");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintVectorType() {
		codeAssembler.printVectorType(5, 3);
		assertBodyText("std_logic_vector(5 downto 3)");
		codeAssembler.printVectorType(2, 2);
		assertBodyText("std_logic_vector(5 downto 3)std_logic_vector(2 downto 2)");
	}
	
	/**
	 * 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPrintZeroElementVectorType() {
		codeAssembler.printVectorType(1, 2);
	}
	
	/**
	 * 
	 */
	@Test
	public void testPrintEntityComment() {
		codeAssembler.printComment("this is foo");
		codeAssembler.beginEntity("bar");
		codeAssembler.endEntity();
		assertBodyText("-- this is foo\nentity bar is\nend bar;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintArchitectureAndSignalComment() {
		codeAssembler.printComment("this is foo");
		codeAssembler.beginArchitecture("bar", "foo");
		codeAssembler.printComment("something else");
		codeAssembler.printScalarSignalDeclaration("yay");
		codeAssembler.beginArchitectureBody();
		codeAssembler.endArchitecture();
		assertBodyText("-- this is foo\narchitecture foo of bar is\n\t-- something else\n\tsignal yay: std_logic;\nbegin\nend foo;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintEmptyClockedProcess() {
		codeAssembler.beginClockedProcess("foo");
		codeAssembler.endClockedProcess();
		assertBodyText("process (foo) begin\nend process;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintClockedProcessWithFakeStatement() {
		codeAssembler.beginClockedProcess("foo");
		codeAssembler.printWholeLine("fake");
		codeAssembler.endClockedProcess();
		assertBodyText("process (foo) begin\n\tfake\nend process;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintIfThen() {
		codeAssembler.beginIfCondition();
		codeAssembler.print("foo");
		codeAssembler.beginThenBranch();
		codeAssembler.printWholeLine("bar;");
		codeAssembler.endIf();
		assertBodyText("if foo then\n\tbar;\nend if;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintIfThenElse() {
		codeAssembler.beginIfCondition();
		codeAssembler.print("foo");
		codeAssembler.beginThenBranch();
		codeAssembler.printWholeLine("bar;");
		codeAssembler.beginElseBranch();
		codeAssembler.printWholeLine("baz;");
		codeAssembler.endIf();
		assertBodyText("if foo then\n\tbar;\nelse\n\tbaz;\nend if;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintIfThenElseIfThen() {
		codeAssembler.beginIfCondition();
		codeAssembler.print("foo");
		codeAssembler.beginThenBranch();
		codeAssembler.printWholeLine("bar;");
		codeAssembler.beginElseIfCondition();
		codeAssembler.print("fup");
		codeAssembler.beginThenBranch();
		codeAssembler.printWholeLine("baz;");
		codeAssembler.endIf();
		assertBodyText("if foo then\n\tbar;\nelseif fup then\n\tbaz;\nend if;\n");
	}

	/**
	 * 
	 */
	@Test
	public void testPrintIfThenElseIfThenElse() {
		codeAssembler.beginIfCondition();
		codeAssembler.print("foo");
		codeAssembler.beginThenBranch();
		codeAssembler.printWholeLine("bar;");
		codeAssembler.beginElseIfCondition();
		codeAssembler.print("fup");
		codeAssembler.beginThenBranch();
		codeAssembler.printWholeLine("baz;");
		codeAssembler.beginElseBranch();
		codeAssembler.printWholeLine("bax;");
		codeAssembler.endIf();
		assertBodyText("if foo then\n\tbar;\nelseif fup then\n\tbaz;\nelse\n\tbax;\nend if;\n");
	}

}
