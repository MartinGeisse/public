/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

/**
 * This interface defines a method that computes a boolean value
 * for an int value.
 */
public interface IIntPredicate {

	/**
	 * Computes a boolean value for the specified int value.
	 * @param value the value for which this predicate shall be evaluated.
	 * @return Returns the result of this predicate for the specified value.
	 */
	public boolean evaluate(int value);
	
}
