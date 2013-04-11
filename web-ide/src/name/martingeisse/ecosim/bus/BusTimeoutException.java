/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

import name.martingeisse.ecosim.util.HexNumberUtil;

/**
 * This exception signals a "bus timeout". This happens when an address
 * is accessed that is not mapped to any attached device.
 */
public class BusTimeoutException extends BusErrorException {

	/**
	 * the serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param address the address that caused a timeout
	 */
	public BusTimeoutException(int address) {
		super("bus timeout for physical address: " + HexNumberUtil.unsignedWordToString(address));
	}

	/**
	 * Constructor
	 * @param message the exception message
	 */
	public BusTimeoutException(String message) {
		super(message);
	}
	
}
