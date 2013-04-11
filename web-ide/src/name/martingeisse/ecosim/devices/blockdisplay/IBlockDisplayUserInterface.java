/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.blockdisplay;

/**
 * This interface must be implemented by the UI to perform display
 * functionality.
 */
public interface IBlockDisplayUserInterface {

	/**
	 * @param blockDisplayUserInterfaceSocket the UI socket of the block display that has changed
	 * @param x the x position of the block to update
	 * @param y the y position of the block to update
	 */
	public void update(IBlockDisplayUserInterfaceSocket blockDisplayUserInterfaceSocket, int x, int y);
	
}
