/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * Produces an IN(...) expression with string
 * values from a String[].
 */
public final class InStringArrayExpression implements IExpression {

	/**
	 * the inputExpression
	 */
	private IExpression inputExpression;
	
	/**
	 * the values
	 */
	private final String[] values;
	
	/**
	 * Constructor.
	 * @param inputExpression the expression to check in an IN clause
	 * @param values the values
	 */
	public InStringArrayExpression(IExpression inputExpression, final String... values) {
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
	public String[] getValues() {
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
			for (String value : values) {
				if (first) {
					first = false;
				} else {
					builder.write(", ");
				}
				builder.writeStringLiteral(value);
			}
			builder.write(") ");
		}
	}

}
