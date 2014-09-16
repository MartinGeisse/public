/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.util;

/**
 * An object than contains a single public variable. This is useful when
 * using inner classes to create closures. In such a case, local variables
 * accessed from the closure must be final, but they may be references
 * to a {@link VariableObject} whose value is NOT final.
 * 
 * @param <T> the type of the variable
 */
public final class VariableObject<T> {

	/**
	 * the value
	 */
	public T value;
	
	/**
	 * Constructor.
	 */
	public VariableObject() {
		this.value = null;
	}
	
	/**
	 * Constructor.
	 * @param value the initial value
	 */
	public VariableObject(T value) {
		this.value = value;
	}
	
}
