/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.mapping;

/**
 * This interface defines a mapping of values of type IN to
 * values of type OUT.
 * 
 * This interface does not specify whether null is an allowed input
 * or output value. Implementations as well as calling code must
 * specify this.
 *
 * @param <IN> the input type
 * @param <OUT> the output type
 */
public interface IMapping<IN, OUT> {

	/**
	 * Maps the specified input value.
	 * 
	 * @param value the value to map
	 * @return the mapped value
	 */
	public OUT map(IN value);
	
}
