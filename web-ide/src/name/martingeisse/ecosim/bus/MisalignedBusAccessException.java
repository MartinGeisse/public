/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * This exception type indicates that the bus was accessed with a
 * misaligned address.
 */
public class MisalignedBusAccessException extends BusErrorException {

	/**
	 * the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param address the misaligned address
	 * @param size the bus access size
	 */
	public MisalignedBusAccessException(int address, BusAccessSize size) {
		super("misaligned bus access at address: " + address + ", size: " + size);
	}
	
}
