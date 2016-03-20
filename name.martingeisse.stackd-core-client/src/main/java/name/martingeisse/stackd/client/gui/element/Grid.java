/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import org.lwjgl.opengl.GL11;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.util.Color;

/**
 * This element is a WxH sized grid that contains pre-generated
 * child elements. Clients must call the initialize() method
 * before using this element to generate the children.
 * 
 * This class requests a zero size from its children to find their
 * minimal size. Then, it places them in the grid assuming that
 * all elements have the same size as the (0, 0) element.
 */
public abstract class Grid extends GuiElement {

	/**
	 * the cellCountX
	 */
	private final int cellCountX;
	
	/**
	 * the cellCountY
	 */
	private final int cellCountY;
	
	/**
	 * the children
	 */
	private GuiElement[] children;
	
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
	 * @param cellCountX the number of cells along the X axis
	 * @param cellCountY the number of cells along the Y axis
	 */
	public Grid(int cellCountX, int cellCountY) {
		if (cellCountX < 0 || cellCountY < 0) {
			throw new IllegalArgumentException("invalid size: " + cellCountX + "*" + cellCountY);
		}
		this.cellCountX = cellCountX;
		this.cellCountY = cellCountY;
		this.color = Color.WHITE;
		this.thickness = 0;
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
	public Grid setColor(Color color) {
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
	public Grid setThickness(int thickness) {
		this.thickness = thickness;
		requestLayout();
		return this;
	}
	
	/**
	 * Generates the child elements.
	 * 
	 * @return this
	 */
	public Grid initialize() {
		this.children = new GuiElement[cellCountX * cellCountY];
		for (int x=0; x<cellCountX; x++) {
			for (int y=0; y<cellCountY; y++) {
				GuiElement child = newChild(x, y);
				children[y * cellCountX + x] = child;
				child.notifyNewParent(this);
			}
		}
		return this;
	}
	
	/**
	 * 
	 */
	private void mustBeInitialized() {
		if (children == null) {
			throw new IllegalStateException("grid has not been initialized yet");
		}
	}
	
	/**
	 * Creates a child element
	 * @param x the x position in the grid
	 * @param y the y position in the grid
	 * @return the child element
	 */
	protected abstract GuiElement newChild(int x, int y);

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		mustBeInitialized();
		for (int x=0; x<cellCountX; x++) {
			for (int y=0; y<cellCountY; y++) {
				children[y * cellCountX + x].handleEvent(event);
			}
		}
		if (event == GuiEvent.DRAW && children.length > 0) {
			GuiElement child = children[0];
			int childLayoutWidth = child.getWidth() + thickness;
			int childLayoutHeight = child.getHeight() + thickness;
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
			color.glColor();
			GL11.glLineWidth(getGui().unitsToPixelsFloat(thickness));
			GL11.glBegin(GL11.GL_LINES);
			int halfThickness = thickness / 2;
			int startX = getAbsoluteX() + halfThickness;
			int startY = getAbsoluteY() + halfThickness;
			int endX = getAbsoluteX() + cellCountX * childLayoutWidth + halfThickness;
			int endY = getAbsoluteY() + cellCountY * childLayoutHeight + halfThickness;
			for (int i=0; i<cellCountX + 1; i++) {
				float x = getAbsoluteX() + i * childLayoutWidth + halfThickness;
				GL11.glVertex2f(x, startY);
				GL11.glVertex2f(x, endY);
			}
			for (int i=0; i<cellCountY + 1; i++) {
				float y = getAbsoluteY() + i * childLayoutHeight + halfThickness;
				GL11.glVertex2f(startX, y);
				GL11.glVertex2f(endX, y);
			}
			GL11.glEnd();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#requestSize(int, int)
	 */
	@Override
	public void requestSize(int width, int height) {
		mustBeInitialized();
		for (int x=0; x<cellCountX; x++) {
			for (int y=0; y<cellCountY; y++) {
				children[y * cellCountX + x].requestSize(0, 0);
			}
		}
		if (cellCountX == 0 || cellCountY == 0) {
			setSize(0, 0);
			return;
		}
		GuiElement child = children[0];
		setSize(cellCountX * child.getWidth() + (cellCountX + 1) * thickness, cellCountY * child.getHeight() + (cellCountY + 1) * thickness);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		mustBeInitialized();
		GuiElement child = children[0];
		int childLayoutWidth = child.getWidth() + thickness;
		int childLayoutHeight = child.getHeight() + thickness;
		for (int x=0; x<cellCountX; x++) {
			for (int y=0; y<cellCountY; y++) {
				children[y * cellCountX + x].setPosition(absoluteX + thickness + x * childLayoutWidth, absoluteY + thickness + y * childLayoutHeight);
			}
		}
	}
	
}
