/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import name.martingeisse.phunky.runtime.code.expression.Expression;

/**
 * This interface is implemented by any object that can handle being
 * called as a function.
 */
public interface PhpCallable {

	/**
	 * Calls this object.
	 * 
	 * @param environment the caller's environment
	 * @param argumentExpressions the argument expressions
	 * @return the return value
	 */
	public Object call(Environment environment, Expression[] argumentExpressions);
	
}
