/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui;


/**
 * The root class for all GUI elements.
 */
public abstract class GuiElement {

	/**
	 * the gui
	 */
	private Gui gui;
	
	/**
	 * the parent
	 */
	private GuiElement parent;
	
	/**
	 * the absoluteX
	 */
	private int absoluteX;
	
	/**
	 * the absoluteY
	 */
	private int absoluteY;
	
	/**
	 * the width
	 */
	private int width;
	
	/**
	 * the height
	 */
	private int height;
	
	/**
	 * Constructor.
	 */
	public GuiElement() {
	}
	
	/**
	 * 
	 */
	final void usedAsRootElement(Gui gui) {
		this.gui = gui;
		this.parent = null;
	}
	
	/**
	 * Setter method for the parent. This method should only be used by the
	 * new parent to inform the child about it.
	 * 
	 * @param parent the parent to set
	 */
	public final void notifyNewParent(GuiElement parent) {
		this.parent = parent;
		this.gui = null;
	}
	
	/**
	 * Getter method for the parent.
	 * @return the parent
	 */
	public final GuiElement getParent() {
		return parent;
	}
	
	/**
	 * Getter method for the gui. Returns null if not added to a gui yet.
	 * @return the gui or null
	 */
	public final Gui getGuiOrNull() {
		if (gui == null && parent != null) {
			gui = parent.getGuiOrNull();
		}
		return gui;
	}

	/**
	 * Getter method for the gui.
	 * @return the gui
	 */
	public final Gui getGui() {
		if (gui == null) {
			if (parent == null) {
				throw new IllegalStateException("This element has not been added to a parent element yet.");
			}
			gui = parent.getGui();
			if (gui == null) {
				throw new IllegalStateException("This element has not been connected to a GUI yet.");
			}
		}
		return gui;
	}

	/**
	 * Handles the specified event.
	 * @param event the event to handle
	 */
	public abstract void handleEvent(GuiEvent event);
	
	/**
	 * Requests a re-layout from the GUI.
	 * 
	 * If no GUI is set, this method does nothing. This is OK because as soon as this
	 * element gets added to a GUI, the new parent element will request a re-layout.
	 */
	protected final void requestLayout() {
		if (gui != null) {
			gui.requestLayout();
		}
	}

	/**
	 * Attempts to set the size of this object based on the size the containing
	 * element has determined for this one. It depends on the actual element what
	 * size is actually set.
	 * 
	 * @param width the width
	 * @param height the height
	 */
	public abstract void requestSize(int width, int height);
	
	/**
	 * Sets the effective size of this element, called from {@link #requestSize(int, int)}.
	 * 
	 * @param width the width to set
	 * @param height the height to set
	 */
	protected final void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public final int getWidth() {
		return width;
	}
	
	/**
	 * Getter method for the height.
	 * @return the height
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Recursively sets the position of this element and all its descendants.
	 * 
	 * @param absoluteX the absolute x position of this element
	 * @param absoluteY the absolute y position of this element
	 */
	public final void setPosition(int absoluteX, int absoluteY) {
		this.absoluteX = absoluteX;
		this.absoluteY = absoluteY;
		setChildrenLayoutPosition(absoluteX, absoluteY);
	}

	/**
	 * Invokes {@link #setPosition(int, int)} on all direct children of this
	 * element after computing their absolute position. The default
	 * implementation does nothing. Elements with children should override
	 * this method.
	 * 
	 * @param absoluteX the absolute x position of this element
	 * @param absoluteY the absolute y position of this element
	 */
	protected void setChildrenLayoutPosition(int absoluteX, int absoluteY) {
	}
	
	/**
	 * Getter method for the absoluteX.
	 * @return the absoluteX
	 */
	public final int getAbsoluteX() {
		return absoluteX;
	}
	
	/**
	 * Getter method for the absoluteY.
	 * @return the absoluteY
	 */
	public final int getAbsoluteY() {
		return absoluteY;
	}

	/**
	 * Determines whether the mouse cursor is currently inside this element.
	 * @return true if inside, false if outside
	 */
	public final boolean isMouseInside() {
		int x = getGui().getMouseX();
		int y = getGui().getMouseY();
		return (x >= absoluteX && x < absoluteX + getWidth() && y >= absoluteY && y < absoluteY + getHeight());
	}
	
}
