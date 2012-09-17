/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

/**
 * A simple ad-hoc consumer interface.
 * 
 * This interface does not specify whether invoking the consume()
 * method zero or multiple times is allowed.
 * 
 * @param <T> the consumed value type
 */
public interface IConsumer<T> {

	/**
	 * Consumes the specified value.
	 * @param value the value to consume
	 */
	public void consume(T value);
	
}
