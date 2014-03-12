/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky;

import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.ExpressionStatement;
import name.martingeisse.phunky.runtime.code.FunctionCall;
import name.martingeisse.phunky.runtime.code.LiteralExpression;
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
			new ExpressionStatement(new FunctionCall("echo", new LiteralExpression("Hello World!")))	
		);
		program.execute(runtime.getGlobalEnvironment());
		
	}
	
}
