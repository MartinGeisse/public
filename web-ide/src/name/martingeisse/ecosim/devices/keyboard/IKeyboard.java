/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.keyboard;

/**
 * This interface represents a keyboard, as seen from the keyboard
 * controller's point of view.
 * 
 * The simulated keyboard controller decides when bytes are received by its
 * own internal simulation timer. Therefore, the keyboard must allow its
 * controller to receive a byte for the device (or signal absence of any
 * new input) at any time.
 */
public interface IKeyboard {

	/**
	 * Checks if any input bytes are available that could be
	 * received through receiveByte().
	 * 
	 * @return Returns true if input is available, false if not.
	 */
	public boolean hasInput();
	
	/**
	 * Receives a byte from the keyboard.
	 * 
	 * @return Returns the received byte.
	 * @throws IllegalStateException if no input is available
	 */
	public byte receiveByte() throws IllegalStateException;

}
