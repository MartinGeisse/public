/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java_cup.runtime.ComplexSymbolFactory.Location;
import name.martingeisse.phunky.runtime.code.expression.EmptyExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.code.expression.FunctionCall;
import name.martingeisse.phunky.runtime.code.expression.operator.CastOperator;

/**
 * Helper functions for the parser.
 */
final class ParserHelper {

	/**
	 * the castOperatorMap
	 */
	private static final Map<String, CastOperator> castOperatorMap = new HashMap<>();

	//
	static {
		castOperatorMap.put("int", CastOperator.INTEGER);
		castOperatorMap.put("integer", CastOperator.INTEGER);
		castOperatorMap.put("bool", CastOperator.BOOLEAN);
		castOperatorMap.put("boolean", CastOperator.BOOLEAN);
		castOperatorMap.put("float", CastOperator.FLOAT);
		castOperatorMap.put("double", CastOperator.FLOAT);
		castOperatorMap.put("real", CastOperator.FLOAT);
		castOperatorMap.put("string", CastOperator.STRING);
		castOperatorMap.put("array", CastOperator.ARRAY);
		castOperatorMap.put("object", CastOperator.OBJECT);
		castOperatorMap.put("unset", CastOperator.NULL);
	}
	
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
			return new EmptyExpression(parameterExpressions.toArray(new Expression[parameterExpressions.size()]));
		} else {
			return new FunctionCall(name, parameterExpressions.toArray(new Expression[parameterExpressions.size()]));
		}
	}
	
	/**
	 * Returns the appropriate {@link CastOperator} for the specified operator
	 * text, or null if not recognized.
	 * 
	 * @param operatorText the operator text
	 * @return the operator
	 */
	public static CastOperator recognizeCastOperator(String operatorText) {
		return castOperatorMap.get(operatorText);
	}

}
