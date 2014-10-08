/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.builtin.var;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * The built-in "unset" function.
 */
public class UnsetFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.PhpCallable#call(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.expression.Expression[])
	 */
	@Override
	public Object call(Environment environment, Expression[] argumentExpressions) {
		for (Expression argumentExpression : argumentExpressions) {
			argumentExpression.bindVariableReference(environment, null);
		}
		return null;
	}
	
}
