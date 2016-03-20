/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.var;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.builtin.BuiltinCallable;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * The built-in "unset" function.
 */
public class UnsetFunction extends BuiltinCallable {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.PhpCallable#call(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.CodeLocation, name.martingeisse.phunky.runtime.code.expression.Expression[])
	 */
	@Override
	public Object call(Environment environment, CodeLocation location, Expression[] argumentExpressions) {
		for (Expression argumentExpression : argumentExpressions) {
			AssignmentTarget target = argumentExpression.resolveAssignmentTarget(environment);
			if (target != null) {
				target.unset();
			}
		}
		return null;
	}
	
}
