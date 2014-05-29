/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.keyboard;

/**
 * This interface represents the behavior of the keyboard controller
 * as seen from the keyboard's point of view.
 */
public interface IKeyboardHost {

	/**
	 * This method is used by the keyboard to notify the controller
	 * about available input bytes that can be fetched via receiveByte().
	 */
	public void onInputAvailable();
	
}
