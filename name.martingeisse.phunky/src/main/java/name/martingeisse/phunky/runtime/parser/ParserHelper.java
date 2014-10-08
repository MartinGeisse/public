/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java_cup.runtime.ComplexSymbolFactory.Location;
import name.martingeisse.phunky.runtime.code.expression.EmptyExpression;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.code.expression.FunctionCall;
import name.martingeisse.phunky.runtime.code.expression.LiteralExpression;
import name.martingeisse.phunky.runtime.code.expression.LocalVariableExpression;
import name.martingeisse.phunky.runtime.code.expression.operator.BinaryExpression;
import name.martingeisse.phunky.runtime.code.expression.operator.BinaryOperator;
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
	public static void error(final Location location, final String message) {
		System.err.println("Syntax error at line " + location.getLine() + ", col " + location.getColumn() + ": " + message);
	}

	/**
	 * Builds an expression from a "<name>(<expression>, ...)" like syntax.
	 * 
	 * @param location the location of the keyword
	 * @param nameExpression the expression that determines the function name
	 * @param parameterExpressions the parameter expressions
	 * @return the expression
	 */
	public static Expression buildFunctionCallLikeExpression(final Location location, final Expression nameExpression, final List<Expression> parameterExpressions) {
		if (nameExpression instanceof LiteralExpression && nameExpression.evaluate(null).equals("empty")) {
			return new EmptyExpression(parameterExpressions.toArray(new Expression[parameterExpressions.size()]));
		} else {
			return new FunctionCall(nameExpression, parameterExpressions.toArray(new Expression[parameterExpressions.size()]));
		}
	}

	/**
	 * Returns the appropriate {@link CastOperator} for the specified operator
	 * text, or null if not recognized.
	 * 
	 * @param operatorText the operator text
	 * @return the operator
	 */
	public static CastOperator recognizeCastOperator(final String operatorText) {
		return castOperatorMap.get(operatorText);
	}

	/**
	 * Parses the contents of a double-quoted string (not including the
	 * double quotes).
	 * 
	 * @param contents the contents
	 * @return the parsed contents
	 */
	public static Expression parseDoubleQuotedStringContents(String contents) {
		if (contents.length() == 0) {
			return new LiteralExpression(contents);
		}
		ArrayList<Expression> segments = new ArrayList<>();
		while (contents.length() > 0) {
			int index = contents.indexOf('$');
			if (index == -1) {
				index = contents.length();
			}
			if (index != 0) {
				segments.add(new LiteralExpression(contents.substring(0, index)));
				contents = contents.substring(index);
			} else {
				index = 1;
				while (index < contents.length()) {
					char c = contents.charAt(index);
					if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_') {
						index++;
					} else {
						break;
					}
				}
				if (index == 1) {
					segments.add(new LiteralExpression("$"));
				} else {
					segments.add(LocalVariableExpression.parse(contents.substring(0, index)));
				}
				contents = contents.substring(index);
			}
		}
		Expression result = segments.get(0);
		for (int i=1; i<segments.size(); i++) {
			result = new BinaryExpression(result, BinaryOperator.CONCATENATE, segments.get(i));
		}
		return result;
	}
	
}
