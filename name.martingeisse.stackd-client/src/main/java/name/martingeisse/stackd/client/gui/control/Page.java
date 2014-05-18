/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.element.FillColor;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.util.AreaAlignment;
import name.martingeisse.stackd.client.gui.util.Color;
import org.apache.log4j.Logger;

/**
 * This element shows a main element over a background filler, and
 * optionally a popup element that can be added/removed/exchanged
 * at runtime.
 * 
 * Pages all to catch all exceptions that occur during event handling
 * in the enclosed elements. These exceptions are passed to
 * {@link #onException(Throwable)}. The default behavior is to catch
 * and log the exceptions.
 * 
 * To support all this, subclasses that want to override
 * {@link #handleEvent(GuiEvent)} must override {@link #handlePageEvent(GuiEvent)}
 * instead.
 */
public class Page extends Control {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(Page.class);
	
	/**
	 * the DARK_OVERLAY
	 */
	private static final Color DARK_OVERLAY = new Color(0, 0, 0, 192);
	
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
		stack.addElement(new FillColor(Color.TRANSPARENT));
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
				FillColor fillColor = (FillColor)getStack().getWrappedElements().get(2);
				fillColor.setColor(popupElement == null ? Color.TRANSPARENT : DARK_OVERLAY);
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
	public final void handleEvent(GuiEvent event) {
		try {
			if (event == GuiEvent.DRAW) {
				super.handleEvent(event);
				return;
			}
			GuiElement popupElement = getPopupElement();
			if (popupElement == null) {
				handlePageEvent(event);
			} else {
				popupElement.handleEvent(event);
			}
		} catch (Throwable t) {
			onException(t);
		}
	}

	/**
	 * Reacts to an event in the page. Only called for non-draw events
	 * if no popup element is visible. Exceptions that occur in this
	 * method are passed to {@link #onException(Throwable)} as usual.
	 * 
	 * The default implementation implements the normal event handling
	 * that passes the event to enclosed elements.
	 * 
	 * When overriding this method, be sure to call
	 * 
	 * 		super.handlePageEvent(event);
	 * 
	 * and not
	 * 
	 * 		super.handleEvent(event);
	 * 
	 * as that would create an infinite loop.
	 * 
	 * @param event the event
	 */
	protected void handlePageEvent(GuiEvent event) {
		super.handleEvent(event);
	}

	/**
	 * This method gets invoked when one of the elements inside the page throw an exception.
	 */
	protected void onException(Throwable t) {
		logger.error("exception during GUI event handling", t);
	}
	
}
