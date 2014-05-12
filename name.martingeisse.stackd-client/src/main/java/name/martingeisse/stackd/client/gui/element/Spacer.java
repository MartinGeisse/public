/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;

/**
 * A simple invisible element with fixed size.
 */
public final class Spacer extends GuiElement {

	/**
	 * Constructor for a square spacer.
	 * @param size the size of the spacer
	 */
	public Spacer(final int size) {
		this(size, size);
	}
	
	/**
	 * Constructor.
	 * @param width the width of the spacer
	 * @param height the height of the spacer
	 */
	public Spacer(final int width, final int height) {
		setSize(width, height);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayout(int, int)
	 */
	@Override
	public void requestSize(int width, int height) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.event.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
	}
	
}
