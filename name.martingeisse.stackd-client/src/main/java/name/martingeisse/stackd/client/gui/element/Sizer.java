/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.util.AreaAlignment;

/**
 * This element works somewhat similar to a {@link Margin}. However, instead of
 * specifying the amount to move inwards from the enclosing element, this
 * element specifies the size and alignment of the inner element directly.
 * 
 * The size along either axis can be negative to disable the sizer's effect
 * and pass the requested size from the enclosing element down to the wrapped
 * element.
 */
public final class Sizer extends AbstractWrapperElement {

	/**
	 * the alignment
	 */
	private AreaAlignment alignment;

	/**
	 * the innerWidth
	 */
	private int innerWidth;

	/**
	 * the innerHeight
	 */
	private int innerHeight;

	/**
	 * Constructor using center alignment.
	 * @param wrappedElement the sized element
	 * @param innerWidth the width of the inner element
	 * @param innerHeight the height of the inner element
	 */
	public Sizer(GuiElement wrappedElement, final int innerWidth, final int innerHeight) {
		this(wrappedElement, innerWidth, innerHeight, AreaAlignment.CENTER);
	}
	
	/**
	 * Constructor.
	 * @param wrappedElement the sized element
	 * @param innerWidth the width of the inner element
	 * @param innerHeight the height of the inner element
	 * @param alignment the alignment of the inner element within the outer one
	 */
	public Sizer(GuiElement wrappedElement, final int innerWidth, final int innerHeight, final AreaAlignment alignment) {
		super(wrappedElement);
		this.innerWidth = innerWidth;
		this.innerHeight = innerHeight;
		this.alignment = alignment;
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
	public Sizer setAlignment(final AreaAlignment alignment) {
		this.alignment = alignment;
		return this;
	}

	/**
	 * Getter method for the innerWidth.
	 * @return the innerWidth
	 */
	public int getInnerWidth() {
		return innerWidth;
	}

	/**
	 * Setter method for the innerWidth.
	 * @param innerWidth the innerWidth to set
	 * @return this for chaining
	 */
	public Sizer setInnerWidth(final int innerWidth) {
		this.innerWidth = innerWidth;
		return this;
	}

	/**
	 * Getter method for the innerHeight.
	 * @return the innerHeight
	 */
	public int getInnerHeight() {
		return innerHeight;
	}

	/**
	 * Setter method for the innerHeight.
	 * @param innerHeight the innerHeight to set
	 * @return this for chaining
	 */
	public Sizer setInnerHeight(final int innerHeight) {
		this.innerHeight = innerHeight;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayout(int, int)
	 */
	@Override
	public void requestSize(final int width, final int height) {
		requireWrappedElement();
		final GuiElement wrappedElement = getWrappedElement();
		wrappedElement.requestSize(innerWidth < 0 ? width : innerWidth, innerHeight < 0 ? height : innerHeight);
		setSize(innerWidth < 0 ? wrappedElement.getWidth() : width, innerHeight < 0 ? wrappedElement.getHeight() : height);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#setChildrenLayoutPosition(int, int)
	 */
	@Override
	protected void setChildrenLayoutPosition(final int absoluteX, final int absoluteY) {
		requireWrappedElement();
		int x = alignment.getHorizontalAlignment().alignSpan(getWidth(), getWrappedElement().getWidth());
		int y = alignment.getVerticalAlignment().alignSpan(getHeight(), getWrappedElement().getHeight());
		getWrappedElement().setPosition(absoluteX + x, absoluteY + y);
	}

}
