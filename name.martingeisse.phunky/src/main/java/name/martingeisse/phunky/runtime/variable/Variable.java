/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.variable;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.oop.PhpObject;

/**
 * A variable that can contain either a value or a {@link MutableVariableContent}.
 *
 * A variable does not simply have a name. Instead, it is referred to
 * by one or more names in one or more {@link Environment}s,
 * {@link PhpVariableArray}s or {@link PhpObject}s.
 * 
 * Values can be arbitrary Java objects that behave as value objects, that is,
 * they are conceptually immutable. Simple values like integers use their
 * java.lang counterpart. Mutable (non-value) contents are distinguished from
 * values by the {@link MutableVariableContent} interface.
 */
public final class Variable {

	/**
	 * the content
	 */
	private Object content;

	/**
	 * Constructor.
	 */
	public Variable() {
		content = null;
	}

	/**
	 * Constructor.
	 * 
	 * @param value the initial value. This must be an immutable value,
	 * not a {@link MutableVariableContent}.
	 */
	public Variable(final Object value) {
		setValue(value);
	}

	/**
	 * Getter method for the (immutable) value of this variable. If this
	 * variable currently contains a {@link MutableVariableContent}, an
	 * immutable copy is made.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return TypeConversionUtil.makeImmutable(content);
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
	 * @param location the location in code, used to trigger errors
	 * @return the variable array
	 */
	public PhpVariableArray getVariableArray(PhpRuntime runtime, CodeLocation location) {
		if (content instanceof PhpVariableArray) {
			return (PhpVariableArray)content;
		} else if (content instanceof PhpValueArray) {
			PhpValueArray valueArray = (PhpValueArray)content;
			PhpVariableArray variableArray = valueArray.toVariableArray();
			content = variableArray;
			return variableArray;
		} else if (TypeConversionUtil.valueCanBeOverwrittenByImplicitArrayConstruction(content)) {
			PhpVariableArray array = new PhpVariableArray();
			content = array;
			return array;
		} else {
			runtime.triggerError("cannot use a scalar value as an array", location);
			return null;
		}
	}

	/**
	 * Setter method for the value.
	 * @param value the value to set
	 */
	public void setValue(final Object value) {
		if (value instanceof MutableVariableContent) {
			throw new RuntimeException("cannot assign a MutableVariableContent to a variable");
		}
		this.content = value;
	}

}
