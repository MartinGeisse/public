/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.BinaryExpression;
import name.martingeisse.phunky.runtime.code.BinaryOperator;
import name.martingeisse.phunky.runtime.code.ExpressionStatement;
import name.martingeisse.phunky.runtime.code.ForStatement;
import name.martingeisse.phunky.runtime.code.FunctionCall;
import name.martingeisse.phunky.runtime.code.FunctionDefinition;
import name.martingeisse.phunky.runtime.code.IfStatement;
import name.martingeisse.phunky.runtime.code.LiteralExpression;
import name.martingeisse.phunky.runtime.code.LocalVariableExpression;
import name.martingeisse.phunky.runtime.code.ReturnStatement;
import name.martingeisse.phunky.runtime.code.StatementSequence;

/**
 * The main class.
 */
public final class Main {

	/**
	 * The main method.
	 * @param args command-line arguments (currently ignored)
	 */
	public static void main(String[] args) {

		PhpRuntime runtime = new PhpRuntime();
		runtime.applyStandardDefinitions();
		StatementSequence program = new StatementSequence(

			// simple tests
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("Hello World!\n"))),	
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression(23))),
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("\n"))),	
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression(42))),
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("\n"))),
			new ExpressionStatement(new FunctionCall("echo", new BinaryExpression(new LiteralExpression(23), BinaryOperator.ADD, new LiteralExpression(42)))),
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("\n"))),
			new ExpressionStatement(new FunctionCall("echo", new LocalVariableExpression("foo"))),
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("\n"))),
			new ExpressionStatement(new BinaryExpression(new LocalVariableExpression("foo"), BinaryOperator.ASSIGN, new LiteralExpression(55))),
			new ExpressionStatement(new FunctionCall("echo", new LocalVariableExpression("foo"))),
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("\n"))),
			
			// the factorial example, of course
			new FunctionDefinition("fac", new String[] {"n"}, new StatementSequence(
				new IfStatement(
					new BinaryExpression(new LocalVariableExpression("n"), BinaryOperator.EQUALS, new LiteralExpression(0)),
					new ReturnStatement(new LiteralExpression(1)),
					new ReturnStatement(new BinaryExpression(new LocalVariableExpression("n"), BinaryOperator.MULTIPLY,
						new FunctionCall("fac", new BinaryExpression(new LocalVariableExpression("n"), BinaryOperator.SUBTRACT, new LiteralExpression(1)))))
				)
			)),
			new ExpressionStatement(new FunctionCall("echo", new FunctionCall("fac", new LiteralExpression(5)))),
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("\n"))),
			
			// for loop
			new ForStatement(
				new ExpressionStatement(new BinaryExpression(new LocalVariableExpression("i"), BinaryOperator.ASSIGN, new LiteralExpression(0))),
				new BinaryExpression(new LocalVariableExpression("i"), BinaryOperator.NOT_EQUALS, new LiteralExpression(10)),
				new ExpressionStatement(new BinaryExpression(new LocalVariableExpression("i"), BinaryOperator.ASSIGN,
					new BinaryExpression(new LocalVariableExpression("i"), BinaryOperator.ADD, new LiteralExpression(1)))),
				new StatementSequence(
					new ExpressionStatement(new FunctionCall("echo", new LocalVariableExpression("i"))),
					new ExpressionStatement(new FunctionCall("echo", new LiteralExpression(", ")))
				)
			),
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("\n"))),
			
			// end
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("ok\n")))
		);
		program.execute(runtime.getGlobalEnvironment());
		
	}
	
}
