/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

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

}
