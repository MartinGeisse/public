/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner;

import name.martingeisse.miner.ingame.IngameHandler;
import name.martingeisse.miner.startmenu.StartmenuHandler;
import name.martingeisse.stackd.client.frame.FrameLoop;
import name.martingeisse.stackd.client.glworker.SimpleWorkerScheme;
import name.martingeisse.stackd.client.util.LwjglNativeLibraryHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * Test Main.
 */
public class Main {

	/**
	 * the screenWidth
	 */
	public static int screenWidth = 800;
	
	/**
	 * the screenHeight
	 */
	public static int screenHeight = 600;
	
	/**
	 * the frameLoop
	 */
	public static FrameLoop frameLoop;
	
	/**
	 * @param args ...
	 * @throws Exception ...
	 */
	public static void main(final String[] args) throws Exception {
		try {

			// parse command-line options
			boolean fullscreen = false;
			for (final String arg : args) {
				if (arg.equals("-fs")) {
					fullscreen = true;
				} else if (arg.equals("-ntg")) {
					IngameHandler.enableTexGen = false;
				} else if (arg.equals("-nt")) {
					IngameHandler.enableTexturing = false;
				} else if (arg.equals("-6")) {
					screenWidth = 640;
					screenHeight = 480;
				} else if (arg.equals("-8")) {
					screenWidth = 800;
					screenHeight = 600;
				} else if (arg.equals("-1")) {
					screenWidth = 1024;
					screenHeight = 768;
				} else if (arg.equals("-12")) {
					screenWidth = 1280;
					screenHeight = 720;
				} else if (arg.equals("-16")) {
					screenWidth = 1680;
					screenHeight = 1050;
				}
			}


			// create a worker loop -- we need to access this in closures
			SimpleWorkerScheme.initialize();
			
			// prepare native libraries
			LwjglNativeLibraryHelper.prepareNativeLibraries();

			// configure the display
	        DisplayMode bestMode = null;
	        int bestModeFrequency = -1;
	        for (DisplayMode mode : Display.getAvailableDisplayModes()) {
	        	if (mode.getWidth() == screenWidth && mode.getHeight() == screenHeight && (mode.isFullscreenCapable() || !fullscreen)) {
	        		if (mode.getFrequency() > bestModeFrequency) {
	        			bestMode = mode;
	        			bestModeFrequency = mode.getFrequency();
	        		}
	        	}
	        }
	        if (bestMode == null) {
	        	bestMode = new DisplayMode(screenWidth, screenHeight);
	        }
	        Display.setDisplayMode(bestMode);
			if (fullscreen) {
				Display.setFullscreen(true);
			}

			// initialize LWJGL
			Display.create(new PixelFormat(0, 24, 0));
			Mouse.create();
			Mouse.poll();

			// build the frame loop
			frameLoop = new FrameLoop(SimpleWorkerScheme.getGlWorkerLoop());
			frameLoop.getRootHandler().setWrappedHandler(new StartmenuHandler());
//			Main.frameLoop.getRootHandler().setWrappedHandler(new IngameHandler());
//			Mouse.setGrabbed(true);

			// run the game logic in a different thread, then run the OpenGL worker in the main thread
			new Thread() {
				@Override
				public void run() {
					frameLoop.executeLoop(null);
					SimpleWorkerScheme.requestStop();
				};
			}.start();
			SimpleWorkerScheme.workAndWait();
			
			// clean up
			Display.destroy();
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
