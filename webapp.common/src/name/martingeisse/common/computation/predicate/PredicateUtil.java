/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.predicate;


/**
 * This class contains utility methods to deal with {@link IPredicate} objects.
 */
public class PredicateUtil {

	/**
	 * the TRUE
	 */
	@SuppressWarnings("rawtypes")
	private static IPredicate TRUE = new IPredicate() {
		@Override
		public boolean evaluate(Object value) {
			return true;
		}
	};

	/**
	 * the FALSE
	 */
	@SuppressWarnings("rawtypes")
	private static IPredicate FALSE = new IPredicate() {
		@Override
		public boolean evaluate(Object value) {
			return false;
		}
	};
	
	/**
	 * Prevent instantiation.
	 */
	private PredicateUtil() {
	}
	
	/**
	 * Returns a predicate that returns true for all input values.
	 * @param <T> the input type
	 * @return the predicate
	 */
	@SuppressWarnings("unchecked")
	public static <T> IPredicate<T> getTrue() {
		return TRUE;
	}
	
	/**
	 * Returns a predicate that returns false for all input values.
	 * @param <T> the input type
	 * @return the predicate
	 */
	@SuppressWarnings("unchecked")
	public static <T> IPredicate<T> getFalse() {
		return FALSE;
	}

}
