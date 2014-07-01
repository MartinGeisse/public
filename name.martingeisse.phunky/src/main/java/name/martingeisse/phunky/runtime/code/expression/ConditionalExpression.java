/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.variable.TypeConversionUtil;

/**
 * Implements the ?..: operator.
 */
public final class ConditionalExpression extends AbstractComputeExpression {

	/**
	 * the condition
	 */
	private final Expression condition;

	/**
	 * the thenExpression
	 */
	private final Expression thenExpression;

	/**
	 * the elseExpression
	 */
	private final Expression elseExpression;

	/**
	 * Constructor.
	 * @param condition the condition
	 * @param thenExpression the expression to evaluate if the condition returns true
	 * @param elseExpression the expression to evaluate if the condition returns false
	 */
	public ConditionalExpression(final Expression condition, final Expression thenExpression, final Expression elseExpression) {
		super();
		this.condition = condition;
		this.thenExpression = thenExpression;
		this.elseExpression = elseExpression;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(final Environment environment) {
		boolean conditionResult = TypeConversionUtil.convertToBoolean(condition.evaluate(environment));
		return (conditionResult ? thenExpression : elseExpression).evaluate(environment);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(final CodeDumper dumper) {
		dumper.print('(');
		condition.dump(dumper);
		dumper.print(" ? ");
		thenExpression.dump(dumper);
		dumper.print(" : ");
		elseExpression.dump(dumper);
		dumper.print(')');
	}

}
