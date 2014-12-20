/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.util.Color;
import org.lwjgl.opengl.GL11;

/**
 * Adds a border around an element that displaces that element and takes
 * up space itself.
 */
public final class ThickBorder extends AbstractWrapperElement {

	/**
	 * the color
	 */
	private Color color;
	
	/**
	 * the thickness
	 */
	private int thickness;

	/**
	 * Constructor.
	 */
	public ThickBorder() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param wrappedElement the wrapped element
	 */
	public ThickBorder(GuiElement wrappedElement) {
		super(wrappedElement);
		this.color = Color.WHITE;
		this.thickness = 1;
	}
	
	/**
	 * Getter method for the color.
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Setter method for the color.
	 * @param color the color to set
	 * @return this for chaining
	 */
	public ThickBorder setColor(Color color) {
		ParameterUtil.ensureNotNull(color, "color");
		this.color = color;
		return this;
	}

	/**
	 * Getter method for the thickness.
	 * @return the thickness
	 */
	public int getThickness() {
		return thickness;
	}
	
	/**
	 * Setter method for the thickness.
	 * @param thickness the thickness to set
	 * @return this for chaining
	 */
	public ThickBorder setThickness(int thickness) {
		this.thickness = thickness;
		requestLayout();
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		requireWrappedElement();
		getWrappedElement().handleEvent(event);
		if (event == GuiEvent.DRAW) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			color.glColor();
			int sizeDelta = getGui().pixelsToUnitsInt(thickness);
			int borderOffset = sizeDelta / 2;
			int x = getAbsoluteX() + borderOffset;
			int y = getAbsoluteY() + borderOffset;
			int w = getWidth() - sizeDelta;
			int h = getHeight() - sizeDelta;
			GL11.glLineWidth(thickness);
			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex2i(x, y);
			GL11.glVertex2i(x + w, y);
			GL11.glVertex2i(x + w, y + h);
			GL11.glVertex2i(x, y + h);
			GL11.glVertex2i(x, y);
			GL11.glEnd();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayoutSize(int, int)
	 */
	@Override
	public void requestSize(int width, int height) {
		int borderSpace = 2 * thickness;
		requireWrappedElement();
		getWrappedElement().requestSize(width + borderSpace, height + borderSpace);
		setSize(getWrappedElement().getWidth() + borderSpace, getWrappedElement().getHeight() + borderSpace);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		requireWrappedElement();
		getWrappedElement().setPosition(absoluteX + thickness, absoluteY + thickness);
	}
	
}
