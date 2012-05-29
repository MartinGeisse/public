/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

/**
 * This interface accepts object of a specific type and returns a boolean
 * value for them.
 * 
 * This interface does not specify whether null is an allowed input value
 * for predicates. APIs that use predicates must define this.
 * 
 * @param <T> the type of accepted objects
 */
public interface IPredicate<T> {

	/**
	 * Evaluates this predicate for the specified input value.
	 * @param input the input value
	 * @return the (boolean) output value
	 */
	public boolean evaluate(T input);
	
}
