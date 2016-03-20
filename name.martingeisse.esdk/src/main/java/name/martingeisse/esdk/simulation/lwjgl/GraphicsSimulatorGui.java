/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.simulation.lwjgl;

import static org.lwjgl.opengl.GL11.glFlush;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * This class allows to quickly build a simulator GUI based
 * on a single LWJGL window.
 * 
 * The gui can detect whether closing it was requested using the
 * window "close" button. However, it is the duty of the application
 * using this GUI to respect that state and stop the main loop, since
 * the main loop is not part of this GUI object.
 */
public abstract class GraphicsSimulatorGui {

	/**
	 * the widthInPixels
	 */
	private final int widthInPixels;
	
	/**
	 * the heightInPixels
	 */
	private final int heightInPixels;
	
	/**
	 * the open
	 */
	private boolean open;
	
	/**
	 * the redrawRequested
	 */
	private boolean redrawRequested;
	
	/**
	 * Constructor.
	 * @param widthInPixels the window width in pixels
	 * @param heightInPixels the window height in pixels
	 */
	public GraphicsSimulatorGui(int widthInPixels, int heightInPixels) {
		this.widthInPixels = widthInPixels;
		this.heightInPixels = heightInPixels;
		this.open = false;
		this.redrawRequested = true;
	}

	/**
	 * Getter method for the widthInPixels.
	 * @return the widthInPixels
	 */
	public int getWidthInPixels() {
		return widthInPixels;
	}
	
	/**
	 * Getter method for the heightInPixels.
	 * @return the heightInPixels
	 */
	public int getHeightInPixels() {
		return heightInPixels;
	}
		
	/**
	 * Opens the simulator GUI.
	 */
	public final void open() throws Exception {
		if (open) {
			throw new IllegalStateException("GUI already opened");
		}
		NativeLibraryHelper.prepareNativeLibraries();
        Display.setDisplayMode(new DisplayMode(widthInPixels, heightInPixels));
		Display.create(new PixelFormat(0, 24, 0));
		Mouse.create();
		Mouse.poll();
		open = true;
	}

	/**
	 * 
	 */
	private void mustBeOpen() {
		if (!open) {
			throw new IllegalStateException("GUI not yet opened");
		}
	}
	
	/**
	 * This method must be called regularly to keep the GUI responsive.
	 * It processes events from the native GUI system, and will also perform
	 * a pending redraw request (if any).
	 */
	public final void respond() {
		mustBeOpen();
		Display.processMessages();
		Keyboard.poll();
		Mouse.poll();
		if (redrawRequested) {
			redraw();
			glFlush();
			Display.update();
			redrawRequested = false;
		}
	}
	
	/**
	 * Getter method for the closeRequested.
	 * @return the closeRequested
	 */
	public boolean isCloseRequested() {
		mustBeOpen();
		return Display.isCloseRequested();
	}
	
	/**
	 * Closes the simulator GUI.
	 */
	public final void close() {
		if (open) {
			Display.destroy();
			open = false;
		}
		redrawRequested = true;
	}
	
	/**
	 * Requests redrawing the window contents. This method should be called
	 * when redrawing would likely produce different contents than visible
	 * now, that is, there is no need to redraw regularly if the contents
	 * don't change.
	 */
	public final void requestRedraw() {
		redrawRequested = true;
	}

	/**
	 * This method should issue the OpenGL commands to redraw the window contents.
	 * Anything that is needed for redrawing besides the actual OpenGL commands,
	 * such as flipping display buffers, is done outside this method.
	 */
	protected abstract void redraw();
	
}
