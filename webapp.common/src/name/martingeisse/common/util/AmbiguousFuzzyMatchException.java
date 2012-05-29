/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import name.martingeisse.common.terms.AmbiguousResultException;

/**
 * This exception indicates that a fuzzy matching algorithm has found
 * multiple matches. Depending on the algorithm, this can be caused both
 * by multiple exact matches or solely by the fuzziness of the algorithm;
 * this exception type does not indicate the difference (though subclasses
 * may do so).
 */
public class AmbiguousFuzzyMatchException extends AmbiguousResultException {

	/**
	 * Constructor.
	 */
	public AmbiguousFuzzyMatchException() {
		super();
	}

	/**
	 * Constructor.
	 * @param message the exception message
	 */
	public AmbiguousFuzzyMatchException(String message) {
		super(message);
	}

}
