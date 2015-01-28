/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame;

import org.lwjgl.input.Keyboard;

/**
 * Base class for frame handlers that can be toggled to be visible
 * or invisible using a key on the keyboard. Subclass handlers should
 * read the visibility flag from this class and act accordingly;
 * no default behavior is implemented by this class -- for example,
 * the draw() method is still called normally even if invisible.
 * 
 * The {@link #handleStep()} method of this class must be called
 * by subclasses to handle the toggle key.
 */
public abstract class AbstractVisiblityToggleFrameHandler extends AbstractFrameHandler {

	/**
	 * the toggleKeyCode
	 */
	private final int toggleKeyCode;
	
	/**
	 * the visible
	 */
	private boolean visible;
	
	/**
	 * the toggleKeyPressed
	 */
	private boolean toggleKeyPressed;
	
	/**
	 * Constructor.
	 * @param toggleKeyCode the key code for the toggle key
	 */
	public AbstractVisiblityToggleFrameHandler(int toggleKeyCode) {
		this(toggleKeyCode, false);
	}
	
	/**
	 * Constructor.
	 * @param toggleKeyCode the key code for the toggle key
	 * @param initiallyVisible whether this handler is initially visible
	 */
	public AbstractVisiblityToggleFrameHandler(int toggleKeyCode, boolean initiallyVisible) {
		this.toggleKeyCode = toggleKeyCode;
		this.visible = initiallyVisible;
		this.toggleKeyPressed = true;
	}
	
	/**
	 * Getter method for the toggleKeyCode.
	 * @return the toggleKeyCode
	 */
	public int getToggleKeyCode() {
		return toggleKeyCode;
	}
	
	/**
	 * Getter method for the visible.
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * Setter method for the visible.
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		boolean toggleKeyPressedNow = Keyboard.isKeyDown(toggleKeyCode);
		if (toggleKeyPressedNow && !toggleKeyPressed) {
			visible = !visible;
		}
		toggleKeyPressed = toggleKeyPressedNow;
	}
	
}
