/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;

/**
 * This element is only visible and only accepts elements when
 * the mouse cursor is over it. Layout is independent of the
 * mouse though.
 */
public final class MouseOverWrapper extends AbstractWrapperElement {

	/**
	 * Constructor.
	 */
	public MouseOverWrapper() {
		super();
	}

	/**
	 * Constructor.
	 * @param wrappedElement the wrapped element
	 */
	public MouseOverWrapper(final GuiElement wrappedElement) {
		super(wrappedElement);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayoutSize(int, int)
	 */
	@Override
	public void requestSize(final int width, final int height) {
		getWrappedElement().requestSize(width, height);
		setSize(getWrappedElement().getWidth(), getWrappedElement().getHeight());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		getWrappedElement().setPosition(absoluteX, absoluteY);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(final GuiEvent event) {
		if (isMouseInside()) {
			getWrappedElement().handleEvent(event);
		}
	}

}
