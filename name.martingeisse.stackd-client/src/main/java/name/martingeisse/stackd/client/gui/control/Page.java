/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.util.AreaAlignment;

/**
 * This element shows a main element over a background filler, and
 * optionally a popup element that can be added/removed/exchanged
 * at runtime.
 */
public class Page extends Control {

	/**
	 * Constructor.
	 * 
	 * @param backgroundElement the background element, or null if none
	 * @param mainElement the main element
	 */
	protected final void initializePage(final GuiElement backgroundElement, final GuiElement mainElement) {
		final OverlayStack stack = new OverlayStack();
		stack.setAlignment(AreaAlignment.CENTER);
		stack.addElement(backgroundElement);
		stack.addElement(mainElement);
		stack.addElement(new Spacer(1));
		setControlRootElement(stack);
	}

	/**
	 * Sets the popup element.
	 * 
	 * @param popupElement the popup element to use, or null for none
	 */
	public final void setPopupElement(final GuiElement popupElement) {
		getGui().addFollowupAction(new Runnable() {
			@Override
			public void run() {
				if (hasPopup()) {
					if (popupElement == null) {
						getStack().removeElement(3);
					} else {
						getStack().replaceElement(3, popupElement);
					}
				} else {
					if (popupElement == null) {
						// nothing to do
					} else {
						getStack().addElement(popupElement);
					}
				}
			}
		});
	}

	/**
	 * 
	 */
	private OverlayStack getStack() {
		return (OverlayStack)getControlRootElement();
	}

	/**
	 * 
	 */
	private boolean hasPopup() {
		return getStack().getWrappedElements().size() > 3;
	}

	/**
	 * Obtains the popup element, if any.
	 * @return the popup element, or null if none
	 */
	public final GuiElement getPopupElement() {
		OverlayStack stack = getStack();
		return (stack.getWrappedElements().size() > 3 ? stack.getWrappedElements().get(3) : null);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.control.Control#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		if (event == GuiEvent.DRAW) {
			super.handleEvent(event);
			return;
		}
		GuiElement popupElement = getPopupElement();
		if (popupElement == null) {
			super.handleEvent(event);
		} else {
			popupElement.handleEvent(event);
		}
	}
	
}
