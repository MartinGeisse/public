/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.assignment;

import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * Abstract representation of "something that can be assigned to".
 * This is different from a variable because of evaulation order:
 * An assignment like these:
 * 		
 * 		LHS = RHS;
 * 		LHS += RHS;
 * 
 * is executed as:
 * - resolve the LHS to an assignment target
 * - evaluate the RHS to a value
 * - resolve the LHS assignment target to a variable
 * - if the operator takes the LHS value as input:
 * 		- read the LHS value from the LHS variable
 * 		- compute the operator
 * - if the LHS is an array-append expression, allocate a new element
 * - set the LHS variable to the RHS value (or computed value)
 */
public interface AssignmentTarget {

	/**
	 * Reads the value of this assignment target (used to implement
	 * computed assignment operators).
	 * 
	 * @param location the location in code, used to trigger errors
	 * @return the value
	 */
	public Object getValue(CodeLocation location);
	
	/**
	 * Assigns a value to this assignment target (used to implement
	 * value assignments).
	 * 
	 * @param location the location in code, used to trigger errors
	 * @param value the value
	 */
	public void assignValue(CodeLocation location, Object value);
	
	/**
	 * Assigns a reference to this assignment target (used to implement
	 * reference assignments).
	 * 
	 * @param location the location in code, used to trigger errors
	 * @param target the reference target
	 */
	public void assignReference(CodeLocation location, Variable target);
	
	/**
	 * Unsets the target.
	 */
	public void unset();
	
}
