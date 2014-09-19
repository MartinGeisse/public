/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.util;

/**
 * This interface is implemented by objects that provide a value.
 * 
 * It can be used, for example, as the high-level equivalent of an HDL signal.
 * 
 * @param <T> the value type
 */
public interface IValueSource<T> {

	/**
	 * @return Returns the current value.
	 */
	public T getValue();
	
}
