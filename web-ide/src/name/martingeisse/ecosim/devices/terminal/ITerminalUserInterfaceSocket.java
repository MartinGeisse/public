/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.terminal;

/**
 * This interface represents a device that is connected to the environment
 * via a (byte-)serial line.
 */
public interface ITerminalUserInterfaceSocket {

	/**
	 * @return Returns the userInterface.
	 */
	public ITerminalUserInterface getUserInterface();

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(ITerminalUserInterface userInterface);

	/**
	 * This method is used by the user interface to notify the serial
	 * line device about available input bytes that can be fetched
	 * via receiveByte().
	 */
	public void onInputAvailable();
	
}
