/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.synthesis.codegen;

import java.io.PrintWriter;

import name.martingeisse.esdk.hdl.PortDirection;

/**
 * VHDL-specific code assembler.
 */
public class VhdlCodeAssembler extends CodeAssembler {

	/**
	 * the currentEntityName
	 */
	private String currentEntityName;

	/**
	 * the currentArchitectureName
	 */
	private String currentArchitectureName;

	/**
	 * the firstPort
	 */
	private boolean firstPort;
	
	/**
	 * Constructor
	 * @param printWriter the print writer used to write VHDL code
	 */
	public VhdlCodeAssembler(PrintWriter printWriter) {
		super(printWriter);
		printLibraryDeclaration("ieee");
		printUseDeclaration("ieee.std_logic_1164.all");
		println();
	}
	
	/**
	 * Ensures that the size of a vector is positive.
	 * @param high the high vector end index
	 * @param low the low vector end index
	 */
	private void checkVectorRange(int high, int low) {
		if (high < low) {
			throw new IllegalArgumentException("invalid vector range: (" + high + " downto " + low + ")");
		}
	}
	
	/**
	 * Prints a "library" declaration for the library with the specified name
	 * @param name the name of the library
	 */
	public void printLibraryDeclaration(String name) {
		printIndentation();
		getPrintWriter().append("library ").append(name).append(";\n");
	}

	/**
	 * Prints a "use" declaration for the library element with the specified name
	 * @param name the name of the library element
	 */
	public void printUseDeclaration(String name) {
		printIndentation();
		getPrintWriter().append("use ").append(name).append(";\n");
	}
	
	/**
	 * Begins an entity with the specified name
	 * @param name the name of the entity
	 */
	public void beginEntity(String name) {
		if (currentEntityName != null) {
			throw new IllegalStateException("not at top level");
		}
		printIndentation();
		print("entity ");
		print(name);
		println(" is");
		this.currentEntityName = name;
		startIndentation();
	}
	
	/**
	 * Ends the current entity.
	 */
	public void endEntity() {
		if (currentEntityName == null) {
			throw new IllegalStateException("no current entity");
		} else if (currentArchitectureName != null) {
			throw new IllegalStateException("currently printing an architecture");
		}
		endIndentation();
		printIndentation();
		print("end ");
		print(currentEntityName);
		println(";");
		this.currentEntityName = null;
	}
	
	/**
	 * Begins a port set declaration.
	 */
	public void beginPorts() {
		printIndentation();
		print("port (");
		firstPort = true;
		startIndentation();
	}
	
	/**
	 * Prints a single scalar port declaration.
	 * @param name the name of the port
	 * @param direction the signal flow direction
	 */
	public void printScalarPortDeclaration(String name, PortDirection direction) {
		
		/** sanity check **/
		if (direction != PortDirection.IN && direction != PortDirection.OUT) {
			throw new IllegalArgumentException("invalid port direction: " + direction);
		}
		
		/** terminate previous line if needed **/
		if (firstPort) {
			firstPort = false;
			println();
		} else {
			println(";");
		}
		
		/** print the port declaration **/
		printIndentation();
		print(name);
		print(": ");
		print(direction == PortDirection.IN ? "in " : "out ");
		printScalarType();
		
	}
	
	/**
	 * Prints a single vector port declaration.
	 * @param name the name of the port
	 * @param direction the signal flow direction
	 * @param high the highest valid vector index
	 * @param low the lowest valid vector index
	 */
	public void printVectorPortDeclaration(String name, PortDirection direction, int high, int low) {
		
		/** sanity check **/
		if (direction != PortDirection.IN && direction != PortDirection.OUT) {
			throw new IllegalArgumentException("invalid port direction: " + direction);
		}
		checkVectorRange(high, low);
		
		/** terminate previous line if needed **/
		if (firstPort) {
			firstPort = false;
			println();
		} else {
			println(";");
		}
		
		/** print the port declaration **/
		printIndentation();
		print(name);
		print(": ");
		print(direction == PortDirection.IN ? "in " : "out ");
		printVectorType(high, low);
		
	}
	
	/**
	 * Ends a port set declaration.
	 */
	public void endPorts() {
		endIndentation();
		println();
		printWholeLine(");");
	}
	
	/**
	 * Begins an architecture.
	 * @param entityName the name of the entity for which this is an architecture
	 * @param architectureName the name of the architecture
	 */
	public void beginArchitecture(String entityName, String architectureName) {
		if (currentEntityName != null) {
			throw new IllegalStateException("not at top level");
		}
		this.currentEntityName = entityName;
		this.currentArchitectureName = architectureName;

		printIndentation();
		print("architecture ");
		print(architectureName);
		print(" of ");
		print(entityName);
		println(" is");
		startIndentation();
	}
	
	/**
	 * Begins the body part of the current architecture.
	 */
	public void beginArchitectureBody() {
		if (currentArchitectureName == null) {
			throw new IllegalStateException("no current architecture");
		}
		endIndentation();
		printWholeLine("begin");
		startIndentation();
	}
	
	/**
	 * Ends the current architecture.
	 */
	public void endArchitecture() {
		if (currentArchitectureName == null) {
			throw new IllegalStateException("no current architecture");
		}
		endIndentation();
		printIndentation();
		print("end ");
		print(currentArchitectureName);
		println(";");
		this.currentEntityName = null;
		this.currentArchitectureName = null;
	}

	/**
	 * Prints a single scalar signal declaration.
	 * @param name the name of the signal
	 */
	public void printScalarSignalDeclaration(String name) {
		printIndentation();
		print("signal ");
		print(name);
		print(": ");
		printScalarType();
		println(";");
	}
	
	/**
	 * Prints a single vector signal declaration.
	 * @param name the name of the signal
	 * @param high the highest valid vector index
	 * @param low the lowest valid vector index
	 */
	public void printVectorSignalDeclaration(String name, int high, int low) {
		checkVectorRange(high, low);
		printIndentation();
		print("signal ");
		print(name);
		print(": ");
		printVectorType(high, low);
		println(";");
	}

	/**
	 * Prints a scalar type expression.
	 */
	public void printScalarType() {
		print("std_logic");
	}

	/**
	 * Prints a vector type expression.
	 * @param high the highest valid vector index
	 * @param low the lowest valid vector index
	 */
	public void printVectorType(int high, int low) {
		checkVectorRange(high, low);
		print("std_logic_vector(");
		print(Integer.toString(high));
		print(" downto ");
		print(Integer.toString(low));
		print(")");
	}
	
	/**
	 * Prints a comment. The argument may contain newline
	 * characters and will generate a multi-line comment in that case.
	 * @param comment the comment to print
	 */
	public void printComment(String comment) {
		for (String line : comment.split("\\n")) {
			printIndentation();
			print("-- ");
			println(line);
		}
	}

	/**
	 * Begins a clocked process that uses the specified clock signal.
	 * @param clockSignalName the name of the clock signal.
	 */
	public void beginClockedProcess(String clockSignalName) {
		printIndentation();
		print("process (");
		print(clockSignalName);
		println(") begin");
		startIndentation();
	}
	
	/**
	 * Ends the current clocked process.
	 */
	public void endClockedProcess() {
		endIndentation();
		printWholeLine("end process;");
	}

	/**
	 * Begins an if statement and prepares generating the condition.
	 */
	public void beginIfCondition() {
		printIndentation();
		print("if ");
	}
	
	/**
	 * Continues an if statement by introducing the 'then'
	 * (or 'elseif-then') branch.
	 */
	public void beginThenBranch() {
		println(" then");
		startIndentation();
	}
	
	/**
	 * Closes the 'then' (or 'elseif-then') branch and prepares
	 * generating an 'elseif' condition.
	 */
	public void beginElseIfCondition() {
		endIndentation();
		printIndentation();
		print("elseif ");
	}
	
	/**
	 * Closes the 'then' (or 'elseif-then') branch and begins
	 * the 'else' branch.
	 */
	public void beginElseBranch() {
		endIndentation();
		printWholeLine("else");
		startIndentation();
	}
	
	/**
	 * Closes the 'then', 'elseif-then' or 'else' branch and
	 * finishes the if statement.
	 */
	public void endIf() {
		endIndentation();
		printWholeLine("end if;");
	}
	
}
