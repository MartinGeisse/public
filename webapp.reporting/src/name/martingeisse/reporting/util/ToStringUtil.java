/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.util;



/**
 * Helper methods to turn objects into strings.
 */
public class ToStringUtil {

	/**
	 * Turns all passed objects to strings using their toString() method and
	 * concatenates the results. The results are returned as a string.
	 * 
	 * The exception are elements that are themselves either an array or an
	 * iterable -- such elements are fed to the corresponding function recursively.
	 * 
	 * @param objects the objects -- must not contain null elements
	 * @return the resulting string
	 */
	public static String concatArray(Object... objects) {
		return concatArray(new StringBuilder(), objects).toString();
	}

	/**
	 * Turns all passed objects to strings using their toString() method and
	 * concatenates the results. The results are stored in the specified builder.
	 * 
	 * The exception are elements that are themselves either an array or an
	 * iterable -- such elements are fed to the corresponding function recursively.
	 * 
	 * @param builder the builder to append to
	 * @param objects the objects -- must not contain null elements
	 * @return the builder argument
	 */
	public static StringBuilder concatArray(StringBuilder builder, Object... objects) {
		for (Object object : objects) {
			concatSingleElement(builder, object);
		}
		return builder;
	}

	/**
	 * Turns all passed objects to strings using their toString() method and
	 * concatenates the results. The results are returned as a string.
	 * 
	 * The exception are elements that are themselves either an array or an
	 * iterable -- such elements are fed to the corresponding function recursively.
	 * 
	 * @param objects the objects -- must not contain null elements
	 * @return the resulting string
	 */
	public static String concatElements(Iterable<?> objects) {
		return concatElements(new StringBuilder(), objects).toString();
	}

	/**
	 * Turns all passed objects to strings using their toString() method and
	 * concatenates the results. The results are stored in the specified builder.
	 * 
	 * The exception are elements that are themselves either an array or an
	 * iterable -- such elements are fed to the corresponding function recursively.
	 * 
	 * @param builder the builder to append to
	 * @param objects the objects -- must not contain null elements
	 * @return the builder argument
	 */
	public static StringBuilder concatElements(StringBuilder builder, Iterable<?> objects) {
		for (Object object : objects) {
			concatSingleElement(builder, object);
		}
		return builder;
	}
	
	/**
	 * If the specified element is either an array or an iterable, then its elements are
	 * turned into strings and the results concatenated via either concatArray() or
	 * concatElements(). Otherwise, the element is turned into a string using its
	 * toString() method. In either case, the result is appended to the specified builder.
	 * @param builder the builder to append to
	 * @param element the element to append
	 * @return the builder
	 */
	public static StringBuilder concatSingleElement(StringBuilder builder, Object element) {
		if (element instanceof Object[]) {
			return concatArray(builder, (Object[])element);
		} else if (element instanceof Iterable<?>) {
			return concatElements(builder, (Iterable<?>)element);
		} else {
			builder.append(element);
			return builder;
		}
	}
	
}
