/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


/**
 * Events of this type are passed down the GUI hierarchy. Any additional
 * data needed for event processing can be obtained from the {@link Gui},
 * {@link Keyboard} and {@link Mouse} classes.
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
	 * This event gets fired when the user releases a key.
	 */
	KEY_RELEASED,
	
	/**
	 * This event gets fired when the user moves the mouse.
	 */
	MOUSE_MOVED,
	
	/**
	 * This event gets fired when the user presses a mouse button.
	 */
	MOUSE_BUTTON_PRESSED,
	
	/**
	 * This event gets fired when the user releases a mouse button.
	 */
	MOUSE_BUTTON_RELEASED;
	
}
