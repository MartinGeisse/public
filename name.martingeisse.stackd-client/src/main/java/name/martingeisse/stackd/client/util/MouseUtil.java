/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.stackd.client.util;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

/**
 * Mouse-related utility methods.
 */
public final class MouseUtil {

	/**
	 * Prevent instantiation.
	 */
	private MouseUtil() {
	}

	/**
	 * Grabs the mouse.
	 */
	public static void grab() {
		while (!Mouse.isGrabbed()) {
			Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
			Mouse.setGrabbed(true);
		}
	}
	
	/**
	 * Un-grabs the mouse.
	 */
	public static void ungrab() {
		while (Mouse.isGrabbed()) {
			Mouse.setGrabbed(false);
		}
	}
	
}
