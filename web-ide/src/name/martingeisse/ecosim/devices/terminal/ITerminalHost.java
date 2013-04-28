/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.terminal;

/**
 * This interface represents the terminal controller, as seen from the terminal.
 */
public interface ITerminalHost {

	/**
	 * This method is used by the user interface to notify the serial
	 * line device about available input bytes that can be fetched
	 * via receiveByte().
	 */
	public void onInputAvailable();
	
}
