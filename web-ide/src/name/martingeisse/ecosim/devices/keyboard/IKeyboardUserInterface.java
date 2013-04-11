/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.keyboard;

/**
 * This interface represents the user interface for a keyboard.
 * 
 * The simulated keyboard controller decides when bytes are received by its
 * own internal simulation timer. Therefore, the user interface must be
 * able to receive a byte for the device (or signal absence of any new input)
 * at any time.
 */
public interface IKeyboardUserInterface {

	/**
	 * Checks if any input bytes are available that could be
	 * received through receiveByte().
	 * @return Returns true if input is available, false if not.
	 */
	public boolean hasInput();
	
	/**
	 * Receives a byte through the user interface
	 * @return Returns the received byte.
	 * @throws IllegalStateException if no input is available
	 */
	public byte receiveByte() throws IllegalStateException;

}
