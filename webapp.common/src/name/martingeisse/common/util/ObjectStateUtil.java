/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * Utility methods to handle object state.
 * 
 * The ensure...() methods check various conditions on object state variables.
 * If invoked for invalid values, they throw an {@link IllegalStateException}.
 */
public class ObjectStateUtil {

	/**
	 * Prevent instantiation.
	 */
	private ObjectStateUtil() {
	}

	/**
	 * Ensures that the specified value is not null because null
	 * is not an allowed state value.
	 * 
	 * @param <T> the static type of the state variable
	 * @param value the state value
	 * @param stateDescription a description of the state variable
	 * @return the state value for convenience
	 */
	public static <T> T nullNotAllowed(final T value, final String stateDescription) {
		if (value == null) {
			throw new IllegalStateException("variable must not be null: " + stateDescription);
		}
		return value;
	}

	/**
	 * Ensures that the specified value is not null because although
	 * null is in principle an allowed state value, it indicates a missing
	 * object that is not supposed to be missing.
	 * 
	 * @param <T> the static type of the state variable
	 * @param value the state value
	 * @param stateDescription a description of the state variable
	 * @return the state value for convenience
	 */
	public static <T> T nullMeansMissing(final T value, final String stateDescription) {
		if (value == null) {
			throw new IllegalStateException("missing object: " + stateDescription);
		}
		return value;
	}
	
}
