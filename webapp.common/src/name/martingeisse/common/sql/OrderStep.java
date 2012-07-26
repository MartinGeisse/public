/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

import name.martingeisse.common.sql.build.ISqlBuilder;
import name.martingeisse.common.sql.expression.IExpression;

/**
 * Default implementation for {@link IOrderStep}. Uses ascending
 * order by default.
 */
public final class OrderStep implements IOrderStep {

	/**
	 * the expression
	 */
	private IExpression expression;

	/**
	 * the direction
	 */
	private OrderDirection direction;

	/**
	 * Constructor.
	 */
	public OrderStep() {
		this(null, OrderDirection.ASCENDING);
	}

	/**
	 * Constructor.
	 * @param expression the expression to order by
	 */
	public OrderStep(final IExpression expression) {
		this(expression, OrderDirection.ASCENDING);
	}

	/**
	 * Constructor.
	 * @param expression the expression to order by
	 * @param direction whether to use ascending or descending order
	 */
	public OrderStep(final IExpression expression, final OrderDirection direction) {
		this.expression = expression;
		this.direction = direction;
	}

	/**
	 * Getter method for the expression.
	 * @return the expression
	 */
	public IExpression getExpression() {
		return expression;
	}

	/**
	 * Setter method for the expression.
	 * @param expression the expression to set
	 */
	public void setExpression(final IExpression expression) {
		this.expression = expression;
	}

	/**
	 * Getter method for the direction.
	 * @return the direction
	 */
	public OrderDirection getDirection() {
		return direction;
	}

	/**
	 * Setter method for the direction.
	 * @param direction the direction to set
	 */
	public void setDirection(final OrderDirection direction) {
		this.direction = direction;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.IOrderStep#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		expression.writeTo(builder);
		builder.write(" ");
		builder.write(direction.getSql());
	}

}
