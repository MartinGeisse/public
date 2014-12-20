/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.NullElement;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.element.ThickBorder;
import name.martingeisse.stackd.client.gui.util.AreaAlignment;

/**
 * Convenience class that combines a border, inner
 * margin and background for a wrapped element.
 *
 * @param <T> the type of wrapped element
 */
public final class Frame<T extends GuiElement> extends Control {

	/**
	 * Constructor.
	 */
	public Frame() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param wrappedElement the wrapped element
	 */
	public Frame(T wrappedElement) {
		OverlayStack stack = new OverlayStack().setAlignment(AreaAlignment.CENTER);
		stack.addElement(new NullElement());
		stack.addElement(new Margin(wrappedElement, 1));
		setControlRootElement(new ThickBorder(stack));
	}
	
	/**
	 * Obtains the border element
	 * @return the border element
	 */
	public ThickBorder getBorder() {
		return (ThickBorder)getControlRootElement();
	}
	
	/**
	 * 
	 */
	protected OverlayStack getStack() {
		return (OverlayStack)getBorder().getWrappedElement();
	}

	/**
	 * Getter method for the background.
	 * @return the background
	 */
	public GuiElement getBackground() {
		return getStack().getWrappedElements().get(0);
	}

	/**
	 * Setter method for the background.
	 * @param background the background to set
	 */
	public void setBackground(GuiElement background) {
		getStack().replaceElement(0, background);
	}

	/**
	 * Obtains the padding (inner margin) element.
	 * @return the padding element
	 */
	public Margin getPadding() {
		return (Margin)getStack().getWrappedElements().get(1);
	}
	
	/**
	 * Getter method for the wrappedElement.
	 * @return the wrappedElement
	 */
	@SuppressWarnings("unchecked")
	public T getWrappedElement() {
		return (T)getPadding().getWrappedElement();
	}
	
	/**
	 * Setter method for the wrappedElement.
	 * @param wrappedElement the wrappedElement to set
	 */
	public void setWrappedElement(T wrappedElement) {
		getPadding().setWrappedElement(wrappedElement);
	}
	
}
