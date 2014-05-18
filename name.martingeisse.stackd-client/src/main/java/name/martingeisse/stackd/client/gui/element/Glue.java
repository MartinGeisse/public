/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.util.AreaAlignment;

/**
 * This element asks its wrapped element to be as small as possible,
 * then takes up the remaining space to behave as the size its
 * enclosing element expects. An {@link AreaAlignment} controls
 * the positioning of the wrapped element.
 * 
 * Boolean parameters are used to control whether any space is
 * actually filled along both the horizontal and vertical axes.
 */
public final class Glue extends AbstractWrapperElement {

	/**
	 * the fillHorizontal
	 */
	private boolean fillHorizontal;

	/**
	 * the fillVertical
	 */
	private boolean fillVertical;

	/**
	 * the alignment
	 */
	private AreaAlignment alignment;

	/**
	 * Constructor using CENTER alignment.
	 */
	public Glue() {
		this(null);
	}
	
	/**
	 * Constructor using CENTER alignment.
	 * @param wrappedElement the wrapped element
	 */
	public Glue(GuiElement wrappedElement) {
		super(wrappedElement);
		this.fillHorizontal = false;
		this.fillVertical = false;
		this.alignment = AreaAlignment.CENTER;
	}

	/**
	 * Getter method for the fillHorizontal.
	 * @return the fillHorizontal
	 */
	public boolean isFillHorizontal() {
		return fillHorizontal;
	}

	/**
	 * Setter method for the fillHorizontal.
	 * @param fillHorizontal the fillHorizontal to set
	 * @return this for chaining
	 */
	public Glue setFillHorizontal(final boolean fillHorizontal) {
		this.fillHorizontal = fillHorizontal;
		return this;
	}

	/**
	 * Getter method for the fillVertical.
	 * @return the fillVertical
	 */
	public boolean isFillVertical() {
		return fillVertical;
	}

	/**
	 * Setter method for the fillVertical.
	 * @param fillVertical the fillVertical to set
	 * @return this for chaining
	 */
	public Glue setFillVertical(final boolean fillVertical) {
		this.fillVertical = fillVertical;
		return this;
	}

	/**
	 * Getter method for the alignment.
	 * @return the alignment
	 */
	public AreaAlignment getAlignment() {
		return alignment;
	}

	/**
	 * Setter method for the alignment.
	 * @param alignment the alignment to set
	 * @return this for chaining
	 */
	public Glue setAlignment(final AreaAlignment alignment) {
		ParameterUtil.ensureNotNull(alignment, "alignment");
		this.alignment = alignment;
		requestLayout();
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayout(int, int)
	 */
	@Override
	public void requestSize(int width, int height) {
		requireWrappedElement();
		final GuiElement wrappedElement = getWrappedElement();
		wrappedElement.requestSize(0, 0);
		width = (fillHorizontal ? Math.max(width, wrappedElement.getWidth()) : wrappedElement.getWidth());
		height = (fillVertical ? Math.max(height, wrappedElement.getHeight()) : wrappedElement.getHeight());
		setSize(width, height);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected void setChildrenLayoutPosition(final int absoluteX, final int absoluteY) {
		requireWrappedElement();
		final GuiElement wrappedElement = getWrappedElement();
		final int dx = alignment.getHorizontalAlignment().alignSpan(getWidth(), wrappedElement.getWidth());
		final int dy = alignment.getVerticalAlignment().alignSpan(getHeight(), wrappedElement.getHeight());
		wrappedElement.setPosition(absoluteX + dx, absoluteY + dy);
	}

}
