/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.blockdisplay;

/**
 * This interface defines the behavior of the block
 * display with respect to its user interface.
 */
public interface IBlockDisplayUserInterfaceSocket {

	/**
	 * @return Returns the userInterface.
	 */
	public IBlockDisplayUserInterface getUserInterface();

	/**
	 * Sets the userInterface.
	 * @param userInterface the new value to set
	 */
	public void setUserInterface(IBlockDisplayUserInterface userInterface);

	/**
	 * @param x the x position of the block to return
	 * @param y the y position of the block to return
	 * @return Returns the block at that position
	 */
	public int getBlock(int x, int y);

	/**
	 * @param x the x position of the block to set
	 * @param y the y position of the block to set
	 * @param b the block to set
	 */
	public void setBlock(int x, int y, int b);

}
