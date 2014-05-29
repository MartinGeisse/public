/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.chardisplay;

/**
 * This interface defines the behavior of the character
 * display controller, as seen from the display's point of view.
 */
public interface ICharacterDisplayHost {

	/**
	 * @param x the x position of the character to return
	 * @param y the y position of the character to return
	 * @return Returns the character at that position
	 */
	public int getCharacter(int x, int y);

	/**
	 * @param x the x position of the character to set
	 * @param y the y position of the character to set
	 * @param c the character to set
	 */
	public void setCharacter(int x, int y, int c);

	/**
	 * @param x the x position of the attribute to return
	 * @param y the y position of the attribute to return
	 * @return Returns the attribute at that position
	 */
	public int getAttribute(int x, int y);
	
	/**
	 * @param x the x position of the attribute to set
	 * @param y the y position of the attribute to set
	 * @param a the attribute to set
	 */
	public void setAttribute(int x, int y, int a);
	
}
