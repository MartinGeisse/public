/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;

/**
 * Base class for a GUI element that wraps a single other element.
 */
public abstract class AbstractWrapperElement extends GuiElement {

	/**
	 * the wrappedElement
	 */
	private GuiElement wrappedElement;

	/**
	 * Constructor.
	 */
	public AbstractWrapperElement() {
	}

	/**
	 * Constructor.
	 * @param wrappedElement the wrapped element
	 */
	public AbstractWrapperElement(GuiElement wrappedElement) {
		setWrappedElement(wrappedElement);
	}

	/**
	 * Getter method for the wrappedElement.
	 * @return the wrappedElement
	 */
	public GuiElement getWrappedElement() {
		return wrappedElement;
	}
	
	/**
	 * Setter method for the wrappedElement.
	 * @param wrappedElement the wrappedElement to set
	 * @return this for chaining
	 */
	public AbstractWrapperElement setWrappedElement(GuiElement wrappedElement) {
		this.wrappedElement = wrappedElement;
		if (wrappedElement != null) {
			wrappedElement.notifyNewParent(this);
		}
		requestLayout();
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.event.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		getWrappedElement().handleEvent(event);
	}

}
