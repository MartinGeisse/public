/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.parser;

import java.util.List;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
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
	 * @param symbol the symbol
	 * @param message the error message
	 */
	public static void error(ComplexSymbol symbol, String message) {
		System.err.println("Syntax error at line " + symbol.xleft.getLine() + ", col " + symbol.xleft.getColumn() + ": " + message);
	}
	
	/**
	 * Builds a statement from a "<keyword> <expression>;" like syntax.
	 * 
	 * @param keywordSymbol the symbol object used to emit errors for the keyword
	 * @param keyword the keyword
	 * @param expression the expression
	 * @return the statement
	 */
	public static Statement buildKeywordExpressionStatement(ComplexSymbol keywordSymbol, String keyword, Expression expression) {
		if (keyword.equals("echo")) {
			return new ExpressionStatement(new FunctionCall("echo", expression));
		} else if (keyword.equals("global")) {
			error(keywordSymbol, "'global' keyword not implemented yet");
			return new NopStatement();
		} else {
			error(keywordSymbol, "unknown keyword: " + keyword);
			return new NopStatement();
		}
	}
	
	/**
	 * Builds an expression from a "<name>(<expression>, ...)" like syntax.
	 * 
	 * @param nameSymbol the symbol object used to emit errors for the function name
	 * @param name the function name
	 * @param parameterExpressions the parameter expressions
	 * @return the expression
	 */
	public static Expression buildFunctionCallLikeExpression(ComplexSymbol nameSymbol, String name, List<Expression> parameterExpressions) {
		if (name.equals("empty")) {
			error(nameSymbol, "'empty' special form not implemented yet");
			return new LiteralExpression(null);
		} else if (name.equals("array")) {
			error(nameSymbol, "'array' special form not implemented yet");
			return new LiteralExpression(null);
		} else {
			return new FunctionCall(name, parameterExpressions.toArray(new Expression[parameterExpressions.size()]));
		}
	}

}
