/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame.handlers;

import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

/**
 * Simple handler that breaks the main loop either when closing the window
 * is requested from the system GUI, or when a certain key is pressed,
 * or when another class wants to exit programmatically.
 */
public final class ExitHandler extends AbstractFrameHandler {

	/**
	 * the breakWhenCloseRequested
	 */
	private final boolean breakWhenCloseRequested;

	/**
	 * the exitKey
	 */
	private final Integer exitKey;

	/**
	 * the programmaticExit
	 */
	private boolean programmaticExit;

	/**
	 * Constructor.
	 * @param breakWhenCloseRequested true to break when the system GUI requests closing
	 * the window, false to ignore such requests
	 * @param exitKey the key code from the {@link Keyboard} class that breaks the
	 * main loop, or null if no key should do that
	 */
	public ExitHandler(final boolean breakWhenCloseRequested, final Integer exitKey) {
		this.breakWhenCloseRequested = breakWhenCloseRequested;
		this.exitKey = exitKey;
		this.programmaticExit = false;
	}

	/**
	 * Getter method for the programmaticExit.
	 * @return the programmaticExit
	 */
	public boolean isProgrammaticExit() {
		return programmaticExit;
	}

	/**
	 * Setter method for the programmaticExit.
	 * @param programmaticExit the programmaticExit to set
	 */
	public void setProgrammaticExit(final boolean programmaticExit) {
		this.programmaticExit = programmaticExit;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.frame.AbstractFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		boolean exit = false;
		if (breakWhenCloseRequested && Display.isCloseRequested()) {
			exit = true;
		}
		if (exitKey != null && Keyboard.isKeyDown(exitKey)) {
			exit = true;
		}
		if (programmaticExit) {
			exit = true;
		}
		if (exit) {
			throw new BreakFrameLoopException();
		}
	}

}
