/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.cpu;

/**
 * This exception type indicates that visualization of a memory
 * cell failed. This error is non-fatal and should result in the
 * corresponding memory cell being visualized using the error
 * message from this exception.
 */
public class MemoryVisualizationException extends Exception {

	/**
	 * the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param message the exception message to show to the user
	 */
	public MemoryVisualizationException(String message) {
		super(message);
	}
	
}
