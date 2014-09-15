/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator;

/**
 * Strategy that defines how the environment reacts to INPUT and OUTPUT
 * instructions executed by a PicoBlaze.
 */
public interface IPicoblazePortHandler {

	/**
	 * Handles an INPUT instruction from the specified address.
	 * @param address the address (0-255)
	 * @return the value (only the lowest 8 bits are respected)
	 */
	public int handleInput(int address);

	/**
	 * Handles an OUTPUT instruction to the specified address.
	 * @param address the address (0-255)
	 * @param value the value (0-255)
	 */
	public void handleOutput(int address, int value);
	
}
