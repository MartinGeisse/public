/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

/**
 * This is a generic exception type that indicates multiple results
 * from a subsystem that was expected to yield only a single result.
 */
public class AmbiguousResultException extends RuntimeException {

	/**
	 * Constructor.
	 */
	public AmbiguousResultException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public AmbiguousResultException(String message) {
		super(message);
	}

}
