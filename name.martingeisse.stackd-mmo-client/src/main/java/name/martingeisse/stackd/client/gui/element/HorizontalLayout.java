/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.util.VerticalAlignment;

/**
 * This layout arranges its elements horizontally, then aligns
 * them vertically using the specified alignment.
 */
public final class HorizontalLayout extends AbstractListElement {

	/**
	 * the alignment
	 */
	private VerticalAlignment alignment;

	/**
	 * Constructor.
	 */
	public HorizontalLayout() {
		this.alignment = VerticalAlignment.CENTER;
	}

	/**
	 * Getter method for the alignment.
	 * @return the alignment
	 */
	public VerticalAlignment getAlignment() {
		return alignment;
	}
	
	/**
	 * Setter method for the alignment.
	 * @param alignment the alignment to set
	 * @return this for chaining
	 */
	public HorizontalLayout setAlignment(VerticalAlignment alignment) {
		ParameterUtil.ensureNotNull(alignment, "alignment");
		this.alignment = alignment;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayoutSize(int, int)
	 */
	@Override
	public void requestSize(int width, int height) {
		int requiredWidth = 0, requiredHeight = height;
		for (GuiElement element : getWrappedElements()) {
			element.requestSize(Gui.GRID, height);
			requiredWidth += element.getWidth();
			requiredHeight = Math.max(requiredHeight, element.getHeight());
		}
		setSize(requiredWidth, requiredHeight);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
		int height = getHeight();
		for (GuiElement element : getWrappedElements()) {
			element.setPosition(absoluteX, absoluteY + alignment.alignSpan(height, element.getHeight()));
			absoluteX += element.getWidth();
		}
	}

}
