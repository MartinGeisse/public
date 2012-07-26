/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Produces an IN(...) expression with integer
 * values from an int[].
 */
public final class InIntegerArrayExpression implements IExpression {

	/**
	 * the inputExpression
	 */
	private IExpression inputExpression;
	
	/**
	 * the values
	 */
	private final int[] values;
	
	/**
	 * Constructor.
	 * @param inputExpression the expression to check in an IN clause
	 * @param values the values
	 */
	public InIntegerArrayExpression(IExpression inputExpression, final int... values) {
		if (inputExpression == null) {
			throw new IllegalArgumentException("'inputExpression' argument is null");
		}
		if (values == null) {
			throw new IllegalArgumentException("'values' argument is null");
		}
		this.inputExpression = inputExpression;
		this.values = values;
	}
	
	/**
	 * Getter method for the inputExpression.
	 * @return the inputExpression
	 */
	public IExpression getInputExpression() {
		return inputExpression;
	}
	
	/**
	 * Getter method for the values.
	 * @return the values
	 */
	public int[] getValues() {
		return values;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		if (values.length == 0) {
			BooleanLiteral.FALSE.writeTo(builder);
		} else {
			builder.write(" ");
			inputExpression.writeTo(builder);
			builder.write(" IN (");
			boolean first = true;
			for (int value : values) {
				if (first) {
					first = false;
				} else {
					builder.write(", ");
				}
				builder.write(Integer.toString(value));
			}
			builder.write(") ");
		}
	}

}
