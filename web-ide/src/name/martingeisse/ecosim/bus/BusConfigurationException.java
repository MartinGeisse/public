/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * This exception type indicates an error in the bus configuration.
 */
public class BusConfigurationException extends RuntimeException {

	/**
	 * the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param message the exception message
	 */
	public BusConfigurationException(String message) {
		super(message);
	}
	
}
