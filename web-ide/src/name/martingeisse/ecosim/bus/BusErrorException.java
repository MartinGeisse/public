/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * This class represents a dynamic bus error, such as an access to an
 * address that is not mapped to a device or a misaligned access.
 */
public class BusErrorException extends Exception {

	/**
	 * the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param message the exception message
	 */
	public BusErrorException(String message) {
		super(message);
	}
	
}
