/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime;

/**
 * This interface is used by various implementation classes to report errors
 * to the outside world. It injects the actual reporting mechanism, which can
 * then be replaced easily.
 */
public interface ErrorReporter {

	/**
	 * Reports an error which could be worked around.
	 * @param message the error message
	 */
	public void reportWarning(String message);
	
	/**
	 * Reports an error that definitely broke something.
	 * @param message the error message
	 */
	public void reportError(String message);
	
}
