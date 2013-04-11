/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.chardisplay;

/**
 * This interface must be implemented by the UI to perform display
 * functionality.
 */
public interface ICharacterDisplayUserInterface {

	/**
	 * @param characterDisplayUserInterfaceSocket the UI socket of the character display that has changed
	 * @param x the x position of the character to update
	 * @param y the y position of the character to update
	 */
	public void update(ICharacterDisplayUserInterfaceSocket characterDisplayUserInterfaceSocket, int x, int y);
	
}
