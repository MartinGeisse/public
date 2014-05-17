/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.element.Glue;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.MouseOverWrapper;
import name.martingeisse.stackd.client.gui.element.NullElement;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.element.PulseFillColor;
import name.martingeisse.stackd.client.gui.element.TextLine;
import name.martingeisse.stackd.client.gui.element.ThinBorder;
import name.martingeisse.stackd.client.gui.util.Color;
import name.martingeisse.stackd.client.gui.util.PulseFunction;

/**
 * A button that can be clicked by the user.
 */
public abstract class Button extends Control {

	/**
	 * the textLine
	 */
	private final TextLine textLine;
	
	/**
	 * the margin
	 */
	private final Margin margin;
	
	/**
	 * the stack
	 */
	private final OverlayStack stack;
	
	/**
	 * the border
	 */
	private final ThinBorder border;
	
	/**
	 * Constructor.
	 * @param fillHorizontal whether the button should fill the available horizontal
	 * space, no matter its natural size
	 * @param fillVertical whether the button should fill the available vertical
	 * space, no matter its natural size
	 */
	public Button(boolean fillHorizontal, boolean fillVertical) {
		textLine = new TextLine();
		margin = new Margin(textLine, Gui.GRID);
		Glue glue = new Glue(margin).setFillHorizontal(fillHorizontal).setFillVertical(fillVertical);
		stack = new OverlayStack();
		stack.addElement(NullElement.instance);
		stack.addElement(glue);
		border = new ThinBorder(stack);
		setControlRootElement(border);
	}
	
	/**
	 * Getter method for the textLine.
	 * @return the textLine
	 */
	public TextLine getTextLine() {
		return textLine;
	}
	
	/**
	 * Getter method for the margin.
	 * @return the margin
	 */
	public Margin getMargin() {
		return margin;
	}
	
	/**
	 * Getter method for the border.
	 * @return the border
	 */
	public ThinBorder getBorder() {
		return border;
	}

	/**
	 * Getter method for the backgroundElement.
	 * @return the backgroundElement
	 */
	public GuiElement getBackgroundElement() {
		return stack.getWrappedElements().get(0);
	}
	
	/**
	 * Setter method for the backgroundElement.
	 * @param backgroundElement the backgroundElement to set
	 */
	public void setBackgroundElement(GuiElement backgroundElement) {
		stack.replaceElement(0, backgroundElement);
	}

	/**
	 * Adds a pulse effect that is visible when the mouse is over the button.
	 * This method doesn't take a pulse amplitude; use the color's alpha
	 * channel for that.
	 * 
	 * @param color the pulse color
	 * @return this button for chaining
	 */
	public Button addPulseEffect(Color color) {
		return addPulseEffect(color, PulseFunction.ABSOLUTE_SINE, 2000);
	}
	
	/**
	 * Adds a pulse effect that is visible when the mouse is over the button.
	 * This method doesn't take a pulse amplitude; use the color's alpha
	 * channel for that.
	 * 
	 * @param color the pulse color
	 * @param function the pulse function
	 * @param period the pulse period
	 * @return this button for chaining
	 */
	public Button addPulseEffect(Color color, PulseFunction function, int period) {
		PulseFillColor fillColor = new PulseFillColor().setColor(color).setPulseFunction(function).setPeriod(period);
		stack.addElement(new MouseOverWrapper(fillColor));
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.control.Control#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		super.handleEvent(event);
		if (event == GuiEvent.MOUSE_BUTTON_PRESSED && isMouseInside()) {
			onClick();
		}
	}
	
	/**
	 * This method gets called when the user clicks on the button.
	 */
	protected abstract void onClick();
	
}
