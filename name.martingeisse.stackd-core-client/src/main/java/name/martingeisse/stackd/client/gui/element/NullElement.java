/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

/**
 * An invisible element that does nothing. This can be used as a
 * placeholder within controls for a place where other elements
 * can go later.
 */
public final class NullElement extends AbstractFillElement {

	/**
	 * The shared instance of this class.
	 */
	public static final NullElement instance = new NullElement();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.element.AbstractFillElement#draw()
	 */
	@Override
	protected void draw() {
	}
	
}
