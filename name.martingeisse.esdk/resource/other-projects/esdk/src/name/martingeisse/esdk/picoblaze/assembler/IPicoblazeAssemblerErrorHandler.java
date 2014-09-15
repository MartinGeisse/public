/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.assembler;


/**
 * This interface is implemented by calling code and used by the
 * assembler to report errors.
 */
public interface IPicoblazeAssemblerErrorHandler {

	/**
	 * Handles an error.
	 * @param range the syntactic range of the error
	 * @param message the error message
	 */
	public void handleError(Range range, String message);

	/**
	 * Handles a warning.
	 * @param range the syntactic range of the warning
	 * @param message the warning message
	 */
	public void handleWarning(Range range, String message);
	
}
