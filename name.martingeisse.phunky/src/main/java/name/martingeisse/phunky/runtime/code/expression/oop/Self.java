/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.expression.oop;

/**
 * Helper method to deal with "self" static calls.
 */
final class Self {

	/**
	 * the SELF
	 */
	static final String SELF = "self".intern();
	
	/**
	 * Similar to {@link String#intern()}, but only interns the string "self".
	 * All other strings are just returned.
	 */
	static String normalize(String s) {
		if (s == SELF || s.equals(SELF)) {
			return SELF;
		} else {
			return s;
		}
	}
	
}
