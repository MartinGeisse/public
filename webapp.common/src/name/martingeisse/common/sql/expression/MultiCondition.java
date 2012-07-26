/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * This is a multi-purpose expression for the "common" cases. It contains a set
 * of boolean sub-expressions and a combination operator (of type
 * {@link MultiConditionOperator}) and provides lots of factory methods for
 * sub-expressions.
 */
public class MultiCondition implements IExpression {

	/**
	 * the operator
	 */
	private final MultiConditionOperator operator;

	/**
	 * the expressions
	 */
	private final List<IExpression> expressions;

	/**
	 * Constructor.
	 */
	public MultiCondition() {
		this(MultiConditionOperator.AND);
	}

	/**
	 * Constructor.
	 * @param operator the combination operator
	 */
	public MultiCondition(final MultiConditionOperator operator) {
		this.operator = operator;
		this.expressions = new ArrayList<IExpression>();
	}

	/**
	 * Constructor.
	 * @param expressions the initial expressions
	 */
	public MultiCondition(final IExpression... expressions) {
		this(MultiConditionOperator.AND, expressions);
	}

	/**
	 * Constructor.
	 * @param operator the combination operator
	 * @param expressions the initial expressions
	 */
	public MultiCondition(final MultiConditionOperator operator, final IExpression... expressions) {
		this.operator = operator;
		this.expressions = Arrays.asList(expressions);
	}

	/**
	 * Constructor.
	 * @param expressions the initial expressions
	 */
	public MultiCondition(final Iterable<IExpression> expressions) {
		this(MultiConditionOperator.AND, expressions);
	}

	/**
	 * Constructor.
	 * @param operator the combination operator
	 * @param expressions the initial expressions
	 */
	public MultiCondition(final MultiConditionOperator operator, final Iterable<IExpression> expressions) {
		this.operator = operator;
		this.expressions = new ArrayList<IExpression>();
		for (final IExpression expression : expressions) {
			this.expressions.add(expression);
		}
	}

	/**
	 * Getter method for the operator.
	 * @return the operator
	 */
	public final MultiConditionOperator getOperator() {
		return operator;
	}

	/**
	 * Getter method for the expressions.
	 * @return the expressions
	 */
	public final List<IExpression> getExpressions() {
		return expressions;
	}

	/**
	 * Creates a simpler replacement for this expression.
	 * @return the replacement
	 */
	public final IExpression createReplacement() {
		return operator.createReplacementExpression(expressions);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.expression.IExpression#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(final ISqlBuilder builder) {
		createReplacement().writeTo(builder);
	}

	/**
	 * Adds the specified expression.
	 * @param expression the expression to add
	 */
	public final void add(final IExpression expression) {
		expressions.add(expression);
	}

	/**
	 * Adds an IS NULL expression for a field (column) value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 */
	public final void addFieldIsNull(final String tableNameOrAlias, final String columnName) {
		expressions.add(new IsNullExpression(new ColumnReference(tableNameOrAlias, columnName)));
	}

	/**
	 * Adds an IS NULL expression for a field (column) value.
	 * @param alias the alias of the field (column or expression)
	 */
	public final void addFieldIsNull(final String alias) {
		expressions.add(new IsNullExpression(new AliasReference(alias)));
	}

	/**
	 * Adds a negated IS NULL expression for a field (column) value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 */
	public final void addFieldIsNotNull(final String tableNameOrAlias, final String columnName) {
		expressions.add(new NotExpression(new IsNullExpression(new ColumnReference(tableNameOrAlias, columnName))));
	}

	/**
	 * Adds a negated IS NULL expression for a field (column) value.
	 * @param alias the alias of the field (column or expression)
	 */
	public final void addFieldIsNotNull(final String alias) {
		expressions.add(new NotExpression(new IsNullExpression(new AliasReference(alias))));
	}

	/**
	 * Adds an IN(...) expression that compares a field (column) value with a set of int values.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param values the values to compare with
	 */
	public final void addFieldInInt(final String tableNameOrAlias, final String columnName, final int... values) {
		expressions.add(new InIntegerArrayExpression(new ColumnReference(tableNameOrAlias, columnName), values));
	}

	/**
	 * Adds an IN(...) expression that compares a field (column) value with a set of int values.
	 * @param alias the alias of the field (column or expression)
	 * @param values the values to compare with
	 */
	public final void addFieldInInt(final String alias, final int... values) {
		expressions.add(new InIntegerArrayExpression(new AliasReference(alias), values));
	}

	/**
	 * Adds a negated IN(...) expression that compares a field (column) value with a set of int values.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param values the values to compare with
	 */
	public final void addFieldNotInInt(final String tableNameOrAlias, final String columnName, final int... values) {
		expressions.add(new NotExpression(new InIntegerArrayExpression(new ColumnReference(tableNameOrAlias, columnName), values)));
	}

	/**
	 * Adds a negated IN(...) expression that compares a field (column) value with a set of int values.
	 * @param alias the alias of the field (column or expression)
	 * @param values the values to compare with
	 */
	public final void addFieldNotInInt(final String alias, final int... values) {
		expressions.add(new NotExpression(new InIntegerArrayExpression(new AliasReference(alias), values)));
	}

	/**
	 * Adds an IN(...) expression that compares a field (column) value with a set of string values.
	 * 
	 * Note: This method does not use a variable-length argument list to avoid ambiguity
	 * in overloading.
	 * 
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param values the values to compare with
	 */
	public final void addFieldInString(final String tableNameOrAlias, final String columnName, final String[] values) {
		expressions.add(new InStringArrayExpression(new ColumnReference(tableNameOrAlias, columnName), values));
	}

	/**
	 * Adds an IN(...) expression that compares a field (column) value with a set of string values.
	 * 
	 * Note: This method does not use a variable-length argument list to avoid ambiguity
	 * in overloading.
	 * 
	 * @param alias the alias of the field (column or expression)
	 * @param values the values to compare with
	 */
	public final void addFieldInString(final String alias, final String[] values) {
		expressions.add(new InStringArrayExpression(new AliasReference(alias), values));
	}

	/**
	 * Adds a negated IN(...) expression that compares a field (column) value with a set of string values.
	 * 
	 * Note: This method does not use a variable-length argument list to avoid ambiguity
	 * in overloading.
	 * 
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param values the values to compare with
	 */
	public final void addFieldNotInString(final String tableNameOrAlias, final String columnName, final String[] values) {
		expressions.add(new NotExpression(new InStringArrayExpression(new ColumnReference(tableNameOrAlias, columnName), values)));
	}

	/**
	 * Adds a negated IN(...) expression that compares a field (column) value with a set of string values.
	 * 
	 * Note: This method does not use a variable-length argument list to avoid ambiguity
	 * in overloading.
	 * 
	 * @param alias the alias of the field (column or expression)
	 * @param values the values to compare with
	 */
	public final void addFieldNotInString(final String alias, final String[] values) {
		expressions.add(new NotExpression(new InStringArrayExpression(new AliasReference(alias), values)));
	}

	/**
	 * Adds a binary expression.
	 * @param left the left sub-expression
	 * @param operator the operator
	 * @param right the right sub-expression
	 */
	public final void addBinary(final IExpression left, final BinaryOperator operator, final IExpression right) {
		expressions.add(new BinaryExpression(left, operator, right));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with another expression.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param operator the comparison operator
	 * @param otherExpression the expression to compare with
	 */
	public final void addFieldComparison(final String tableNameOrAlias, final String columnName, final BinaryOperator operator, final IExpression otherExpression) {
		addBinary(new ColumnReference(tableNameOrAlias, columnName), operator, otherExpression);
	}

	/**
	 * Adds an expression that compares a field (column or expression) value for equality with another expression.
	 * @param alias the alias of the field (column or expression)
	 * @param operator the comparison operator
	 * @param otherExpression the expression to compare with
	 */
	public final void addFieldComparison(final String alias, final BinaryOperator operator, final IExpression otherExpression) {
		addBinary(new AliasReference(alias), operator, otherExpression);
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with another expression.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param otherExpression the expression to compare with
	 */
	public final void addFieldEquals(final String tableNameOrAlias, final String columnName, final IExpression otherExpression) {
		addFieldComparison(tableNameOrAlias, columnName, BinaryOperator.EQUAL, otherExpression);
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with another expression.
	 * @param alias the alias of the field (column or expression)
	 * @param otherExpression the expression to compare with
	 */
	public final void addFieldEquals(final String alias, final IExpression otherExpression) {
		addFieldComparison(alias, BinaryOperator.EQUAL, otherExpression);
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with another expression.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param otherExpression the expression to compare with
	 */
	public final void addFieldNotEquals(final String tableNameOrAlias, final String columnName, final IExpression otherExpression) {
		addFieldComparison(tableNameOrAlias, columnName, BinaryOperator.NOT_EQUAL, otherExpression);
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with another expression.
	 * @param alias the alias of the field (column or expression)
	 * @param otherExpression the expression to compare with
	 */
	public final void addFieldNotEquals(final String alias, final IExpression otherExpression) {
		addFieldComparison(alias, BinaryOperator.NOT_EQUAL, otherExpression);
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with a boolean value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param operator the comparison operator
	 * @param value the value to compare with
	 */
	public final void addFieldComparison(final String tableNameOrAlias, final String columnName, final BinaryOperator operator, final boolean value) {
		addFieldComparison(tableNameOrAlias, columnName, operator, createBooleanLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column or expression) value for equality with a boolean value.
	 * @param alias the alias of the field (column or expression)
	 * @param operator the comparison operator
	 * @param value the value to compare with
	 */
	public final void addFieldComparison(final String alias, final BinaryOperator operator, final boolean value) {
		addFieldComparison(alias, operator, createBooleanLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with a boolean value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param value the value to compare with
	 */
	public final void addFieldEquals(final String tableNameOrAlias, final String columnName, final boolean value) {
		addFieldEquals(tableNameOrAlias, columnName, createBooleanLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with a boolean value.
	 * @param alias the alias of the field (column or expression)
	 * @param value the value to compare with
	 */
	public final void addFieldEquals(final String alias, final boolean value) {
		addFieldEquals(alias, createBooleanLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with a boolean value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param value the value to compare with
	 */
	public final void addFieldNotEquals(final String tableNameOrAlias, final String columnName, final boolean value) {
		addFieldNotEquals(tableNameOrAlias, columnName, createBooleanLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with a boolean value.
	 * @param alias the alias of the field (column or expression)
	 * @param value the value to compare with
	 */
	public final void addFieldNotEquals(final String alias, final boolean value) {
		addFieldNotEquals(alias, createBooleanLiteral(value));
	}

	/**
	 * TODO: Format boolean literals on the right side of comparisons as integer in MySQL
	 */
	private IExpression createBooleanLiteral(final boolean value) {
		return BooleanLiteral.from(value);
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with an integer value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param operator the comparison operator
	 * @param value the value to compare with
	 */
	public final void addFieldComparison(final String tableNameOrAlias, final String columnName, final BinaryOperator operator, final int value) {
		addFieldComparison(tableNameOrAlias, columnName, operator, new IntegerLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column or expression) value for equality with an integer value.
	 * @param alias the alias of the field (column or expression)
	 * @param operator the comparison operator
	 * @param value the value to compare with
	 */
	public final void addFieldComparison(final String alias, final BinaryOperator operator, final int value) {
		addFieldComparison(alias, operator, new IntegerLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with an integer value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param value the value to compare with
	 */
	public final void addFieldEquals(final String tableNameOrAlias, final String columnName, final int value) {
		addFieldEquals(tableNameOrAlias, columnName, new IntegerLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with an integer value.
	 * @param alias the alias of the field (column or expression)
	 * @param value the value to compare with
	 */
	public final void addFieldEquals(final String alias, final int value) {
		addFieldEquals(alias, new IntegerLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with an integer value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param value the value to compare with
	 */
	public final void addFieldNotEquals(final String tableNameOrAlias, final String columnName, final int value) {
		addFieldNotEquals(tableNameOrAlias, columnName, new IntegerLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with an integer value.
	 * @param alias the alias of the field (column or expression)
	 * @param value the value to compare with
	 */
	public final void addFieldNotEquals(final String alias, final int value) {
		addFieldNotEquals(alias, new IntegerLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with a string value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param operator the comparison operator
	 * @param value the value to compare with
	 */
	public final void addFieldComparison(final String tableNameOrAlias, final String columnName, final BinaryOperator operator, final String value) {
		addFieldComparison(tableNameOrAlias, columnName, operator, new StringLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column or expression) value for equality with a string value.
	 * @param alias the alias of the field (column or expression)
	 * @param operator the comparison operator
	 * @param value the value to compare with
	 */
	public final void addFieldComparison(final String alias, final BinaryOperator operator, final String value) {
		addFieldComparison(alias, operator, new StringLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with a string value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param value the value to compare with
	 */
	public final void addFieldEquals(final String tableNameOrAlias, final String columnName, final String value) {
		addFieldEquals(tableNameOrAlias, columnName, new StringLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for equality with a string value.
	 * @param alias the alias of the field (column or expression)
	 * @param value the value to compare with
	 */
	public final void addFieldEquals(final String alias, final String value) {
		addFieldEquals(alias, new StringLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with a string value.
	 * @param tableNameOrAlias the name or alias of the table
	 * @param columnName the name of the column
	 * @param value the value to compare with
	 */
	public final void addFieldNotEquals(final String tableNameOrAlias, final String columnName, final String value) {
		addFieldNotEquals(tableNameOrAlias, columnName, new StringLiteral(value));
	}

	/**
	 * Adds an expression that compares a field (column) value for inequality with a string value.
	 * @param alias the alias of the field (column or expression)
	 * @param value the value to compare with
	 */
	public final void addFieldNotEquals(final String alias, final String value) {
		addFieldNotEquals(alias, new StringLiteral(value));
	}

}
