/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime;

import name.martingeisse.phunky.runtime.oop.PhpObject;
import name.martingeisse.phunky.runtime.value.PhpArray;

/**
 * A variable that can contain a value.
 * 
 * Values can be arbitrary Java objects. Simple values like
 * integers use their java.lang counterpart.
 *
 * A variable does not simply have a name. Instead, it is referred to
 * by one or more names in one or more {@link Environment}s,
 * {@link PhpArray}s or {@link PhpObject}s.
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
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setter method for the value.
	 * @param value the value to set
	 */
	public void setValue(final Object value) {
		this.value = value;
	}

}
