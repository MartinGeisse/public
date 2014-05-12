/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import java.util.ArrayList;
import java.util.List;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;

/**
 * Base class for a GUI element that wraps a list of other elements.
 * 
 * This element allows to obtain the internal list, but note that
 * if you modify that list directly, you must also request a
 * re-layout manually.
 */
public abstract class AbstractListElement extends GuiElement {

	/**
	 * the wrappedElements
	 */
	private final List<GuiElement> wrappedElements;
	
	/**
	 * Constructor.
	 */
	public AbstractListElement() {
		this.wrappedElements = new ArrayList<>();
	}
	
	/**
	 * Constructor.
	 * @param elements the elements to add to the list
	 */
	public AbstractListElement(GuiElement... elements) {
		this.wrappedElements = new ArrayList<>();
		for (GuiElement element : elements) {
			addElement(element);
		}
	}
	
	/**
	 * Getter method for the wrappedElements.
	 * @return the wrappedElements
	 */
	public final List<GuiElement> getWrappedElements() {
		return wrappedElements;
	}
	
	/**
	 * Adds the specified element. This method requests a re-layout automatically.
	 * 
	 * @param element the element to add
	 * @return this for chaining
	 */
	public final AbstractListElement addElement(GuiElement element) {
		wrappedElements.add(element);
		element.notifyNewParent(this);
		requestLayout();
		return this;
	}
	
	/**
	 * Replaces the specified element. This method requests a re-layout automatically.
	 * @param index the index of the element to replace
	 * @param newElement the new element
	 * @return this for chaining
	 */
	public final AbstractListElement replaceElement(int index, GuiElement newElement) {
		getWrappedElements().get(index).notifyNewParent(null);
		getWrappedElements().set(index, newElement);
		newElement.notifyNewParent(this);
		requestLayout();
		return this;
	}

	/**
	 * Clears the list of wrapped elements. This method requests a re-layout
	 * automatically.
	 */
	public final void clearElements() {
		wrappedElements.clear();
		requestLayout();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.event.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		for (GuiElement element : getWrappedElements()) {
			element.handleEvent(event);
		}
	}

}
