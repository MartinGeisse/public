/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.test.json;

import java.util.LinkedList;

import org.junit.Assert;

/**
 * Implementations are able to test JSON values for an implementation-specific
 * kind of equivalence. JSON values are represented as:
 * 
 * - null
 * - java.lang.Boolean
 * - java.lang.Number
 * - java.lang.String
 * - java.util.List<Object>
 * - java.util.Map<String, Object>
 */
public interface IJsonEquivalence {

	/**
	 * Asserts that two JSON values are equivalent. This method is preferred over
	 * {@link Assert#assertTrue(boolean)} on the result of {@link #equivalent(Object, Object)}
	 * because it gives more detailed error information.
	 * 
	 * @param x the first value to compare
	 * @param y the second value to compare
	 * @param contextStack a stack of descriptive strings used to show the context of errors
	 */
	public void assertEquivalent(Object x, Object y, LinkedList<String> contextStack);
	
	/**
	 * Checks whether two values are equivalent.
	 * 
	 * @param x the first value to compare
	 * @param y the second value to compare
	 * @return true if equivalent, false if not
	 */
	public boolean equivalent(Object x, Object y);
	
}
