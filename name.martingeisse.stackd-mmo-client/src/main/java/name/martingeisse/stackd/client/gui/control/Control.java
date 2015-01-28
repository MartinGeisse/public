/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;

/**
 * Base class to build higher-level controls from primitive elements.
 */
public class Control extends GuiElement {

	/**
	 * the controlRootElement
	 */
	private GuiElement controlRootElement;

	/**
	 * Constructor.
	 */
	public Control() {
	}

	/**
	 * Getter method for the controlRootElement.
	 * @return the controlRootElement
	 */
	protected final GuiElement getControlRootElement() {
		return controlRootElement;
	}
	
	/**
	 * Setter method for the controlRootElement.
	 * @param controlRootElement the controlRootElement to set
	 */
	protected final void setControlRootElement(GuiElement controlRootElement) {
		this.controlRootElement = controlRootElement;
		if (controlRootElement != null) {
			controlRootElement.notifyNewParent(this);
		}
		requestLayout();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		controlRootElement.handleEvent(event);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayoutSize(int, int)
	 */
	@Override
	public final void requestSize(int width, int height) {
		controlRootElement.requestSize(width, height);
		setSize(controlRootElement.getWidth(), controlRootElement.getHeight());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected final void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		controlRootElement.setPosition(absoluteX, absoluteY);
	}
	
}
