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
import name.martingeisse.phunky.runtime.code.statement.ExpressionStatement;
import name.martingeisse.phunky.runtime.code.statement.NopStatement;
import name.martingeisse.phunky.runtime.code.statement.Statement;

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
	 * Builds a statement from a "<keyword> <expression>;" like syntax.
	 * 
	 * @param location the location of the keyword
	 * @param keyword the keyword
	 * @param expression the expression
	 * @return the statement
	 */
	public static Statement buildKeywordExpressionStatement(Location location, String keyword, Expression expression) {
		if (keyword.equals("echo")) {
			return new ExpressionStatement(new FunctionCall("echo", expression));
		} else if (keyword.equals("global")) {
			error(location, "'global' keyword not implemented yet");
			return new NopStatement();
		} else {
			error(location, "unknown keyword: " + keyword);
			return new NopStatement();
		}
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
		} else if (name.equals("array")) {
			error(location, "'array' special form not implemented yet");
			return new LiteralExpression(null);
		} else {
			return new FunctionCall(name, parameterExpressions.toArray(new Expression[parameterExpressions.size()]));
		}
	}

}
