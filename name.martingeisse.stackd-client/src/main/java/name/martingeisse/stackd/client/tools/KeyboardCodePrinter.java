/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.tools;

import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.frame.FrameLoop;
import name.martingeisse.stackd.client.util.LwjglNativeLibraryHelper;
import name.martingeisse.stackd.client.util.MouseUtil;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * Opens a window, lets you hit keys on the keyboard, and prints the
 * LWJGL keyboard code for each.
 */
public final class KeyboardCodePrinter {

	/**
	 * The main method.
	 * @param args ignored
	 */
	public static void main(String[] args) {
		try {

			// prepare native libraries
			LwjglNativeLibraryHelper.prepareNativeLibraries();

			// configure the display
			Display.setDisplayMode(new DisplayMode(800, 600));

			// initialize LWJGL
			Display.create();
			Mouse.create();
			Mouse.poll();
			MouseUtil.grab();

			final FrameLoop loop = new FrameLoop();
			loop.getRootHandler().setWrappedHandler(new AbstractFrameHandler() {
				@Override
				public void handleStep() throws BreakFrameLoopException {
					while (Keyboard.next()) {
						System.out.println("" + System.currentTimeMillis() + " code " + Keyboard.getEventKey() + " char '" + Keyboard.getEventCharacter() + "' (" + ((int)Keyboard.getEventCharacter()) + ")");
						if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
							throw new BreakFrameLoopException();
						}
					}
				}
			});
			loop.executeLoop(null);

			Display.destroy();
			System.exit(0);

		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
}
