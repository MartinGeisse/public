/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace;

import name.martingeisse.stackd.client.frame.FrameLoop;
import name.martingeisse.stackd.client.glworker.SimpleWorkerScheme;
import name.martingeisse.stackd.client.util.LwjglNativeLibraryHelper;
import name.martingeisse.stackd.common.task.TaskSystem;
import name.martingeisse.stackerspace.ingame.IngameHandler;
import name.martingeisse.stackerspace.ingame.StackerspaceResources;
import org.apache.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * Test Main.
 */
public class Main {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(Main.class);
	
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
		logger.info("Stackerspace client started");
		try {
			
			// initialize task system so we can start up in parallel tasks
			Thread.currentThread().setName("Startup (later OpenGL)");
			TaskSystem.initialize();

			// parse command-line options
			logger.trace("parsing command line options...");
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
			logger.trace("command line options parsed");

			// create a worker loop -- we need to access this in closures
			logger.trace("initializing OpenGL worker scheme...");
			SimpleWorkerScheme.initialize();
			logger.trace("OpenGL worker scheme initialized");
			
			// prepare native libraries
			logger.trace("preparing native libraries...");
			LwjglNativeLibraryHelper.prepareNativeLibraries();
			logger.trace("native libraries prepared");

			// configure the display
			logger.trace("finding optimal display mode...");
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
			logger.trace("setting intended display mode...");
	        Display.setDisplayMode(bestMode);
			if (fullscreen) {
				Display.setFullscreen(true);
			}
			logger.trace("switching display mode...");
			Display.create(new PixelFormat(0, 24, 0));
			logger.trace("display initialized");

			// initialize LWJGL
			logger.trace("preparing mouse...");
			Mouse.create();
			Mouse.poll();
			logger.trace("mouse prepared");
			
			// load images and sounds
			logger.trace("loading resources...");
			StackerspaceResources.initializeInstance();
			logger.trace("resources loaded...");

			// build the frame loop
			frameLoop = new FrameLoop(SimpleWorkerScheme.getGlWorkerLoop());
			frameLoop.getRootHandler().setWrappedHandler(new IngameHandler());
			
			// run the game logic in a different thread, then run the OpenGL worker in the main thread
			new Thread("Application") {
				@Override
				public void run() {
					frameLoop.executeLoop(null);
					SimpleWorkerScheme.requestStop();
				};
			}.start();
			logger.debug("startup thread becoming the OpenGL thread now");
			Thread.currentThread().setName("OpenGL");
			SimpleWorkerScheme.workAndWait();
			
			// clean up
			Display.destroy();
			
		} catch (final Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
