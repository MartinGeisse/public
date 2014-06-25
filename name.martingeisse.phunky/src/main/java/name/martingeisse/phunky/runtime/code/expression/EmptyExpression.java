/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.value.TypeConversionUtil;

/**
 * Represents the empty(...) special form.
 */
public class EmptyExpression extends AbstractCallExpression {

	/**
	 * the isVariable
	 */
	private final boolean isVariable;
	
	/**
	 * Constructor.
	 * @param parameters the parameter expressions
	 */
	public EmptyExpression(final Expression[] parameters) {
		super(parameters);
		if (parameters.length != 1) {
			throw new IllegalArgumentException("empty() requires exactly one parameter");
		}
		this.isVariable = (parameters[0] instanceof AbstractVariableExpression);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#evaluate(name.martingeisse.phunky.runtime.Environment)
	 */
	@Override
	public Object evaluate(Environment environment) {
		
		TODO .isEmpty()
		
		if (isVariable) {
			Variable variable = ((AbstractVariableExpression)getParameter(0)).getVariable(environment);
			if (variable == null) {
				return false;
			} else {
				return TypeConversionUtil.convertToBoolean(variable.getValue());
			}
		} else {
			return TypeConversionUtil.convertToBoolean(getParameter(0).evaluate(environment));
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.code.expression.Expression#dump(name.martingeisse.phunky.runtime.code.CodeDumper)
	 */
	@Override
	public void dump(CodeDumper dumper) {
		dumper.print("empty(");
		getParameter(0).dump(dumper);
		dumper.print(")");
	}

}
