/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.variable.location;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * {@link AssignmentTarget} implementation for array-append expressions.
 */
public class ArrayAppendVariableLocation implements VariableLocation {

	/**
	 * the arrayLocation
	 */
	private final VariableLocation arrayLocation;

	/**
	 * Constructor.
	 * @param arrayLocation the location to get the array from
	 */
	public ArrayAppendVariableLocation(VariableLocation arrayLocation) {
		this.arrayLocation = arrayLocation;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.location.VariableLocation#getVariable(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public Variable getVariable(Environment environment, CodeLocation codeLocation) {
		Variable arrayVariable = arrayLocation.getVariable(environment, codeLocation);
		if (arrayVariable == null) {
			return null;
		}
		TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.location.VariableLocation#setVariable(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.CodeLocation, name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void setVariable(Environment environment, CodeLocation codeLocation, Variable variable) {
		TODO
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#getValue(name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public Object getValue(CodeLocation location) {
		throw new UnsupportedOperationException("cannot get the value from an array-append expression");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignValue(name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object)
	 */
	@Override
	public void assignValue(CodeLocation location, Object value) {
		array.append().setValue(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignReference(name.martingeisse.phunky.runtime.code.CodeLocation, name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void assignReference(CodeLocation location, Variable target) {
		array.append(target);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#unset()
	 */
	@Override
	public void unset() {
		throw new UnsupportedOperationException("cannot unset an array-append expression");
	}

}
