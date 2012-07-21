/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.predicate;


/**
 * This class contains utility methods to deal with {@link String}-typed
 * {@link IPredicate} objects.
 */
public class StringPredicateUtil {

	/**
	 * Prevent instantiation.
	 */
	private StringPredicateUtil() {
	}
	
	/**
	 * This predicate returns true if the input string contains only digits.
	 * Precisely, this predicate does not use {@link Character}.isDigit() but
	 * looks for ASCII digits ('0' through '9') only.
	 */
	public static final IPredicate<String> digitsOnly = new IPredicate<String>() {
		@Override
		public boolean evaluate(String value) {
			for (int i=0; i<value.length(); i++) {
				char c = value.charAt(i);
				if (c < '0' || c > '9') {
					return false;
				}
			}
			return true;
		}
	};
	
}
