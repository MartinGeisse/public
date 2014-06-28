/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;
import name.martingeisse.phunky.runtime.code.CodeDumper;

/**
 * An expression is an object that can be evaluated using an
 * {@link Environment}, mostly to obtain the resulting value
 * but in some cases for side-effects on the environment and
 * on other objects.
 * 
 * Some expressions cannot just be evaluated, but they also
 * denote a variable to which values can be assigned. For those
 * expressions, this interface also allows to obtain that
 * variable, e.g. for assignment or to build a reference
 * to it.
 */
public interface Expression {

	/**
	 * Evaluates this expression.
	 * @param environment the environment
	 * @return the value of the expression
	 */
	public Object evaluate(Environment environment);

	/**
	 * Evaluates this expression, but returns null for missing variables.
	 * This is used to implement PHP's empty().
	 * 
	 * @param environment the environment
	 * @return the value of the expression, or null
	 */
	public Object evaluateForEmptyCheck(Environment environment);

	/**
	 * Obtains the variable for this expression, if any.
	 * @param environment the environment
	 * @return the variable or null
	 */
	public Variable getVariable(Environment environment);
	
	/**
	 * Obtains the variable for this expression; creates the variable if there could be
	 * one but isn't; returns null if this expression cannot denote a variable.
	 * 
	 * @param environment the environment
	 * @return the variable or null
	 */
	public Variable getOrCreateVariable(Environment environment);

	/**
	 * Dumps this expression using the specified code dumper.
	 * @param dumper the code dumper
	 */
	public void dump(CodeDumper dumper);
	
}
