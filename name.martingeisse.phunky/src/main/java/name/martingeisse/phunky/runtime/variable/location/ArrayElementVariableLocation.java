/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.variable.location;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.assignment.AssignmentTarget;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.Variable;
import name.martingeisse.phunky.util.ParameterUtil;

/**
 * {@link AssignmentTarget} implementation for array elements.
 * 
 * TODO probably still not correct since PHP would probably defer
 * resolution of the array variable too.
 */
public class ArrayElementVariableLocation implements VariableLocation {

	/**
	 * the arrayLocation
	 */
	private final VariableLocation arrayLocation;

	/**
	 * the key
	 */
	private final String key;

	/**
	 * Constructor.
	 * @param arrayLocation the location to get the array from
	 * @param key the key
	 */
	public ArrayElementVariableLocation(VariableLocation arrayLocation, String key) {
		this.arrayLocation = arrayLocation;
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.location.VariableLocation#getVariable(name.martingeisse.phunky.runtime.Environment, name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public Variable getVariable(Environment environment, CodeLocation codeLocation) {
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

	/**
	 * Constructor.
	 * @param arrayVariable the variable containing the array (or string)
	 */
	public ArrayElementVariableLocation(Variable arrayVariable, String key) {
		this.arrayVariable = ParameterUtil.ensureNotNull(arrayVariable, "arrayVariable");
		this.key = ParameterUtil.ensureNotNull(key, "key");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#getValue(name.martingeisse.phunky.runtime.code.CodeLocation)
	 */
	@Override
	public Object getValue(CodeLocation location) {
		return arrayVariable.getVariableArray(runtime, location).getValue(key);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignValue(name.martingeisse.phunky.runtime.code.CodeLocation, java.lang.Object)
	 */
	@Override
	public void assignValue(CodeLocation location, Object value) {
		arrayVariable.getVariableArray(runtime, location).getVariable(key).setValue(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignReference(name.martingeisse.phunky.runtime.code.CodeLocation, name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void assignReference(CodeLocation location, Variable target) {
		arrayVariable.getVariableArray(runtime, location).setVariable(key, target);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#unset()
	 */
	@Override
	public void unset() {
		arrayVariable.getVariableArray(runtime, location).removeVariable(key);
	}

}
