/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.terminal;

/**
 * This interface represents a terminal, as seen from the terminal controller.
 * 
 * The serial line device decides when bytes are sent or received by its
 * own internal simulation timer. Therefore, the user interface must be
 * able to receive characters sent by the device at any time, and be able
 * to receive a byte for the device (or signal absence of any new input)
 * at any time.
 */
public interface ITerminal {

	/**
	 * Sends a byte through the user interface
	 * @param b the byte to send
	 */
	public void sendByte(byte b);

	/**
	 * Sends a corrupted byte through the user interface. This indicates
	 * that transmission of a byte has started, but the device was
	 * not allowed to send the byte completely. 
	 */
	public void sendCorruptedByte();

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
