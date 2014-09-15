/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.core;

/**
 * This interface is implemented by components that produce a value.
 * It is the high-level equivalent of an HDL signal.
 * 
 * @param <T> the value type
 */
public interface IValueSource<T> {

	/**
	 * @return Returns the current value.
	 */
	public T getValue();
	
}
