/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.pixeldisplay;

/**
 * This interface defines the behavior of the pixel
 * display with respect to its user interface.
 */
public interface IPixelDisplayUserInterfaceSocket {

	/**
	 * @return Returns the userInterface.
	 */
	public IPixelDisplayUserInterface getUserInterface();

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(IPixelDisplayUserInterface userInterface);

	/**
	 * @param x the x position of the pixel to return
	 * @param y the y position of the pixel to return
	 * @return Returns the pixel at that position
	 */
	public int getPixel(int x, int y);

	/**
	 * @param x the x position of the pixel to set
	 * @param y the y position of the pixel to set
	 * @param p the pixel to set
	 */
	public void setPixel(int x, int y, int p);
	
}
