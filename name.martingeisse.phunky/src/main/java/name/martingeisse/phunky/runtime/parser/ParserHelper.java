/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.parser;

import java.util.List;
import java_cup.runtime.ComplexSymbolFactory.Location;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.code.expression.FunctionCall;
import name.martingeisse.phunky.runtime.code.expression.LiteralExpression;

/**
 * Helper functions for the parser.
 */
final class ParserHelper {

	/**
	 * Prevent instantiation.
	 */
	private ParserHelper() {
	}
	
	/**
	 * Emits an error for the specified symbol.
	 * 
	 * @param location the location of the symbol
	 * @param message the error message
	 */
	public static void error(Location location, String message) {
		System.err.println("Syntax error at line " + location.getLine() + ", col " + location.getColumn() + ": " + message);
	}
	
	/**
	 * Builds an expression from a "<name>(<expression>, ...)" like syntax.
	 * 
	 * @param location the location of the keyword
	 * @param name the function name
	 * @param parameterExpressions the parameter expressions
	 * @return the expression
	 */
	public static Expression buildFunctionCallLikeExpression(Location location, String name, List<Expression> parameterExpressions) {
		if (name.equals("empty")) {
			error(location, "'empty' special form not implemented yet");
			return new LiteralExpression(null);
		} else {
			return new FunctionCall(name, parameterExpressions.toArray(new Expression[parameterExpressions.size()]));
		}
	}

}
