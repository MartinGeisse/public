/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.assignment;

import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * {@link AssignmentTarget} implementation for array elements.
 */
public class ArrayElementAssignmentTarget implements AssignmentTarget {

	/**
	 * the array
	 */
	private final PhpVariableArray array;

	/**
	 * the key
	 */
	private final String key;

	/**
	 * Constructor.
	 * @param array the array
	 * @param key the key
	 */
	public ArrayElementAssignmentTarget(PhpVariableArray array, String key) {
		this.array = array;
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#getValue()
	 */
	@Override
	public Object getValue() {
		return array.getValue(key);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignValue(java.lang.Object)
	 */
	@Override
	public void assignValue(Object value) {
		array.getVariable(key).setValue(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignReference(name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void assignReference(Variable target) {
		array.setVariable(key, target);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#unset()
	 */
	@Override
	public void unset() {
		array.removeVariable(key);
	}

}
