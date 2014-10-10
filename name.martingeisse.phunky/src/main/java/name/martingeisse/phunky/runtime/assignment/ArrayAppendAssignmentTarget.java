/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.assignment;

import name.martingeisse.phunky.runtime.variable.PhpVariableArray;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * {@link AssignmentTarget} implementation for array-append expressions.
 */
public class ArrayAppendAssignmentTarget implements AssignmentTarget {

	/**
	 * the array
	 */
	private final PhpVariableArray array;

	/**
	 * Constructor.
	 * @param array the array
	 */
	public ArrayAppendAssignmentTarget(PhpVariableArray array) {
		this.array = array;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#getValue()
	 */
	@Override
	public Object getValue() {
		throw new UnsupportedOperationException("cannot get the value from an array-append expression");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignValue(java.lang.Object)
	 */
	@Override
	public void assignValue(Object value) {
		array.append().setValue(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.assignment.AssignmentTarget#assignReference(name.martingeisse.phunky.runtime.variable.Variable)
	 */
	@Override
	public void assignReference(Variable target) {
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
