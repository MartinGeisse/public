/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui;

/**
 * Events of this type are passed down the GUI hierarchy. Any additional
 * data needed for event processing can be obtained from the {@link Gui}
 * for the current event.
 */
public enum GuiEvent {

	/**
	 * This event gets fired to draw the GUI.
	 */
	DRAW,
	
	/**
	 * This event gets fired when the user presses a key.
	 */
	KEY_PRESSED,
	
	/**
	 * This event gets fired when the user moves the mouse.
	 */
	MOUSE_MOVED,
	
	/**
	 * This event gets fired when the user clicks the mouse.
	 */
	MOUSE_CLICKED;
	
}
