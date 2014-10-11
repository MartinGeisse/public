/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;
import name.martingeisse.phunky.runtime.variable.Variable;

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
	 * Getter method for the location.
	 * @return the location
	 */
	public CodeLocation getLocation();
	
	/**
	 * Setter method for the location.
	 * @param location the location to set
	 */
	public void setLocation(CodeLocation location);
	
	/**
	 * Evaluates this expression. This is used when this expression is
	 * used as part of another expression or as the value source in an
	 * assignment.
	 * 
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
	 * Obtains the variable for this expression; creates the variable if there could be
	 * one but isn't; returns null if this expression cannot denote a variable.
	 * This is used to implement the right-hand side for reference
	 * assignments.
	 * 
	 * Note that this cannot be used to implement the left-hand side of a
	 * value assignment because the evaluation order would be wrong.
	 * The target variable of a value assignment is resolved partly before,
	 * partly after resolving the right-hand side. Use {@link #resolveAssignmentTarget(Environment)}
	 * to implement the "before" part and use the value acceptor itself
	 * to implement the "after" part.
	 * 
	 * @param environment the environment
	 * @return the variable or null
	 */
	public Variable resolveOrCreateVariable(Environment environment);
	
	/**
	 * Obtains the value acceptor for this expression; returns null if this
	 * expression cannot denote a value acceptor. This is used to
	 * implement the left-hand side of both value assignments and
	 * reference assignments.
	 * 
	 * @param environment the environment
	 * @return the value acceptor or null
	 */
	public AssignmentTarget resolveAssignmentTarget(Environment environment);
	
	/**
	 * Dumps this expression using the specified code dumper.
	 * @param dumper the code dumper
	 */
	public void dump(CodeDumper dumper);

	/**
	 * Converts this expression to a JSON representation of the code, by using
	 * the provided JSON builder to generate a JSON value that represents this
	 * expression.
	 * 
	 * @param builder the JSON builder to use for the conversion
	 */
	public void toJson(JsonValueBuilder<?> builder);

}
