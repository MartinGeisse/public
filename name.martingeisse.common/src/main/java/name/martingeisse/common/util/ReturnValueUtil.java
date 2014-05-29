/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * Utility methods to handle method return values. This is mostly useful
 * to ensure that methods have actually returned what they're supposed to return.
 * 
 * The ensure...() methods check various conditions on returned values.
 * If invoked for invalid arguments, they throw an {@link IllegalReturnValueException}.
 * 
 */
public class ReturnValueUtil {

	/**
	 * Prevent instantiation.
	 */
	private ReturnValueUtil() {
	}

	/**
	 * Ensures that the specified return value is not null because null
	 * is not an allowed return value.
	 * 
	 * @param <T> the static return type
	 * @param value the return value
	 * @param methodName the name of the method that returned the value (for error messages)
	 * @return the return value for convenience
	 */
	public static <T> T nullNotAllowed(final T value, final String methodName) {
		if (value == null) {
			throw new IllegalReturnValueException("method returned null but that return value is not allowed: " + methodName);
		}
		return value;
	}

	/**
	 * Ensures that the specified return value is not null because although
	 * null is in principle an allowed return value, it indicates a missing
	 * object that is not supposed to be missing.
	 * 
	 * @param <T> the static return type
	 * @param value the return value
	 * @param objectDescription the name of the object that is expected to be returned
	 * @return the return value for convenience
	 */
	public static <T> T nullMeansMissing(final T value, final String objectDescription) {
		if (value == null) {
			throw new RuntimeException("missing object: " + objectDescription);
		}
		return value;
	}
	
}
