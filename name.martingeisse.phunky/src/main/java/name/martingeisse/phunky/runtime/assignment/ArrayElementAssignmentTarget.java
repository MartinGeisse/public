/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.assignment;

import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
import name.martingeisse.phunky.runtime.variable.Variable;
import name.martingeisse.phunky.util.ParameterUtil;

/**
 * {@link AssignmentTarget} implementation for array elements.
 * 
 * TODO probably still not correct since PHP would probably defer
 * resolution of the array variable too.
 */
public class ArrayElementAssignmentTarget implements AssignmentTarget {

	/**
	 * the arrayVariable
	 */
	private final Variable arrayVariable;

	/**
	 * the key
	 */
	private final String key;

	/**
	 * Constructor.
	 * @param arrayVariable the variable containing the array (or string)
	 * @param key the key
	 */
	public ArrayElementAssignmentTarget(Variable arrayVariable, String key) {
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
