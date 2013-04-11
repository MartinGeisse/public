/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.pixeldisplay;

/**
 * This interface must be implemented by the UI to perform pixel
 * display functionality.
 */
public interface IPixelDisplayUserInterface {

	/**
	 * @param pixelDisplayUserInterfaceSocket the UI socket of the pixel display that has changed
	 * @param x the x position of the pixel to update
	 * @param y the y position of the pixel to update
	 */
	public void update(IPixelDisplayUserInterfaceSocket pixelDisplayUserInterfaceSocket, int x, int y);
	
}
