/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.List;

/**
 * Utility methods to handle method parameters.
 * 
 * The ensure...() methods check various parameter conditions.
 * If invoked for invalid arguments, they throw an
 * {@link IllegalArgumentException}.
 * 
 */
public class ParameterUtil {

	/**
	 * Prevent instantiation.
	 */
	private ParameterUtil() {
	}

	/**
	 * Ensures that the specified argument is not null.
	 * @param <T> the static parameter type
	 * @param argument the argument value
	 * @param name the argument name (for error messages)
	 * @return the argument value for convenience
	 */
	public static <T> T ensureNotNull(final T argument, final String name) {
		if (argument == null) {
			throw new IllegalArgumentException("argument is null: " + name);
		}
		return argument;
	}

	/**
	 * Ensures that the specified array argument does not contain any null element.
	 * 
	 * This method does not ensure that the array itself is a non-null reference;
	 * instead, it will simply skip the element check if the array itself is null.
	 * 
	 * @param <T> the static array element type
	 * @param argument the argument value (may be null)
	 * @param name the argument name (for error messages)
	 * @return the argument value for convenience
	 */
	public static <T> T[] ensureNoNullElement(final T[] argument, final String name) {
		if (argument != null) {
			for (int i=0; i<argument.length; i++) {
				if (argument[i] == null) {
					throw new IllegalArgumentException("argument contains null element at index " + i + ": " + name);
				}
			}
		}
		return argument;
	}

	/**
	 * Ensures that the specified list argument does not contain any null element.
	 * 
	 * This method does not ensure that the list itself is a non-null reference;
	 * instead, it will simply skip the element check if the list itself is null.
	 * 
	 * @param <T> the static list element type
	 * @param argument the argument value (may be null)
	 * @param name the argument name (for error messages)
	 * @return the argument value for convenience
	 */
	public static <T> List<T> ensureNoNullElement(final List<T> argument, final String name) {
		if (argument != null) {
			int i=0;
			for (T element : argument) {
				if (element == null) {
					throw new IllegalArgumentException("argument contains null element at index " + i + ": " + name);
				}
				i++;
			}
		}
		return argument;
	}

	/**
	 * Ensures that the specified collection argument does not contain any null element.
	 * 
	 * If the collection is a list, you should use {@link #ensureNoNullElement(List, String)}
	 * instead since, in case of errors, it will also include the index where null was found
	 * as part of the exception message.
	 * 
	 * This method does not ensure that the collection itself is a non-null reference;
	 * instead, it will simply skip the element check if the collection itself is null.
	 * 
	 * @param <T> the element type
	 * @param <I> the iterable type
	 * @param argument the argument value (may be null)
	 * @param name the argument name (for error messages)
	 * @return the argument value for convenience
	 */
	public static <T, I extends Iterable<T>> I ensureNoNullElement(final I argument, final String name) {
		if (argument != null) {
			for (T element : argument) {
				if (element == null) {
					throw new IllegalArgumentException("argument contains null element: " + name);
				}
			}
		}
		return argument;
	}
	
}
