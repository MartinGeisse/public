/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.variable;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.oop.PhpObject;

/**
 * A variable that can contain a value.
 *
 * A variable does not simply have a name. Instead, it is referred to
 * by one or more names in one or more {@link Environment}s,
 * {@link PhpVariableArray}s or {@link PhpObject}s.
 * 
 * Values can be arbitrary Java objects. Simple values like
 * integers use their java.lang counterpart.
 * 
 * Values are either assumed to be immutable, and thus allowed to
 * be transparently shared, or are mutable and implement {@link MutableValue}.
 * This interface is used to distinguish the two cases.
 */
public final class Variable {

	/**
	 * the value
	 */
	private Object value;

	/**
	 * Constructor.
	 */
	public Variable() {
		value = null;
	}

	/**
	 * Constructor.
	 * @param value the initial value
	 */
	public Variable(final Object value) {
		setValue(value);
	}

	/**
	 * Getter method for the (immutable) value of this variable. If this
	 * variable currently contains a mutable value, and immutable copy
	 * is made.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		if (value instanceof MutableValue) {
			MutableValue mutableValue = (MutableValue)value;
			return mutableValue.createImmutableCopy();
		} else {
			return value;
		}
	}

	/**
	 * Obtains a variable array from this variable. If this variable currently
	 * contain a value array, a variable array is created from it. If it contains
	 * any other value except null or false, then an error is triggered and this
	 * method returns null because such values cannot be overwritten with an
	 * array implicitly. Null and false values are silently overwritten with
	 * a newly created variable array, which is then returned.
	 * 
	 * @param runtime the runtime, used to trigger errors
	 * @return the variable array
	 */
	public PhpVariableArray getVariableArray(PhpRuntime runtime) {
		if (value instanceof PhpVariableArray) {
			return (PhpVariableArray)value;
		} else if (value instanceof PhpValueArray) {
			PhpValueArray array = (PhpValueArray)value;
			return array.toVariableArray();
		} else if (TypeConversionUtil.valueCanBeOverwrittenByImplicitArrayConstruction(value)) {
			PhpVariableArray array = new PhpVariableArray();
			value = array;
			return array;
		} else {
			runtime.triggerError("cannot use a scalar value as an array");
			return null;
		}
	}
	
	/**
	 * Setter method for the value.
	 * @param value the value to set
	 */
	public void setValue(final Object value) {
		if (value instanceof MutableValue) {
			throw new RuntimeException("cannot assign a MutableValue to a variable");
		}
		this.value = value;
	}

}
