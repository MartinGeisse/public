/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;

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
			return;
		}
		GuiElement child = children[0];
		setSize(cellCountX * child.getWidth(), cellCountY * child.getHeight());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		mustBeInitialized();
		GuiElement child = children[0];
		int childWidth = child.getWidth();
		int childHeight = child.getHeight();
		for (int x=0; x<cellCountX; x++) {
			for (int y=0; y<cellCountY; y++) {
				children[y * cellCountX + x].setPosition(absoluteX + x * childWidth, absoluteY + y * childHeight);
			}
		}
	}
	
}
