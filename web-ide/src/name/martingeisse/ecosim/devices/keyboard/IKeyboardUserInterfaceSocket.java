/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.keyboard;

/**
 * This interface represents the behavior of a keyboard towards its
 * user interface.
 */
public interface IKeyboardUserInterfaceSocket {

	/**
	 * @return Returns the userInterface.
	 */
	public IKeyboardUserInterface getUserInterface();

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(IKeyboardUserInterface userInterface);

	/**
	 * This method is used by the user interface to notify the keyboard
	 * about available input bytes that can be fetched via receiveByte().
	 */
	public void onInputAvailable();
	
}
