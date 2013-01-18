/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.test.json;

import java.util.LinkedList;

import org.junit.Assert;

/**
 * Provides a simple default implementation for 
 * {@link IJsonEquivalence#assertEquivalent(Object, Object, LinkedList)}.
 */
public abstract class AbstractJsonEquivalence implements IJsonEquivalence {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.util.test.json.IJsonEquivalence#assertEquivalent(java.lang.Object, java.lang.Object, java.util.LinkedList)
	 */
	@Override
	public void assertEquivalent(Object x, Object y, LinkedList<String> contextStack) {
		if (!equivalent(x, y)) {
			String message = "JSON values are not equivalent: [" + x.getClass() + "], [" + y.getClass() + "]\n" + x + "\nand\n" + y;
			System.out.println(message);
			Assert.fail(message);
		}
	}

	/**
	 * Checks if two elements of the value currently being compared are equivalent. This method is
	 * used to implement both assertion and normal equivalence checking: if an assert context stack
	 * is specified, this method will call {@link Assert#fail(String)} on non-equivalence,
	 * otherwise it just returns a boolean value.
	 * 
	 * @param x the element of the first value
	 * @param y the element of the second value
	 * @param assertContextStack the context stack, or null to just return a boolean value
	 * @param extraContext the context element for the value element
	 * @return true if equivalent, false or exception otherwise
	 */
	protected final boolean checkElementsEquivalent(Object x, Object y, LinkedList<String> assertContextStack, String extraContext) {
		if (assertContextStack == null) {
			return equivalent(x, y);
		} else {
			assertContextStack.push(extraContext);
			assertEquivalent(x, y, assertContextStack);
			assertContextStack.pop();
			return true;
		}
	}
	
	/**
	 * Emits an error about non-equivalent values or returns false, depending on whether an
	 * assert context stack is passed.
	 * 
	 * @param x the first value
	 * @param y the second value
	 * @param assertContextStack the context stack, or null to just return false
	 * @param message the error message
	 * @return false if context stack is null (throws an exception otherwise)
	 */
	protected final boolean nonEquivalent(Object x, Object y, LinkedList<String> assertContextStack, Object message) {
		if (assertContextStack == null) {
			return false;
		}
		StringBuilder builder = new StringBuilder();
		builder.append("values not equivalent [" + x.getClass() + "], [" + y.getClass() + "]: " + message + " -- at ");
		boolean first = true;
		for (String element : assertContextStack) {
			if (first) {
				first = false;
			} else {
				builder.append('.');
			}
			builder.append(element);
		}
		builder.append(":\n").append(x).append("\nvs.\n").append(y);
		System.out.println(builder.toString());
		Assert.fail(builder.toString());
		return false;
	}

	/**
	 * Emits an error about non-equivalent values or returns false, depending on whether an
	 * assert context stack is passed.
	 * 
	 * @param x the first value
	 * @param y the second value
	 * @param assertContextStack the context stack, or null to just return false
	 * @param extraContext an implicit context element
	 * @param message the error message
	 * @return false if context stack is null (throws an exception otherwise)
	 */
	protected final boolean nonEquivalent(Object x, Object y, LinkedList<String> assertContextStack, String extraContext, Object message) {
		if (assertContextStack == null) {
			return false;
		}
		assertContextStack.push(extraContext);
		return nonEquivalent(extraContext, y, assertContextStack, message);
	}
	
}
