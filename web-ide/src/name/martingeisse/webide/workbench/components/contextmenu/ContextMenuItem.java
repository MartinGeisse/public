/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components.contextmenu;

/**
 * Base class for context menu items.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public abstract class ContextMenuItem<A> {

	/**
	 * Constructor.
	 */
	ContextMenuItem() {
	}

	/**
	 * Triggers the effect of this menu item.
	 * @param anchor the anchor
	 */
	public final void notifySelected(A anchor) {
		notifySelectedInternal(anchor);
	}
	
	/**
	 * This method is invoked when the item is selected.
	 */
	abstract void notifySelectedInternal(A anchor);

	/**
	 * Internal method used to generate the code for this menu.
	 * @param builder the string builder to append to
	 */
	abstract void buildItem(StringBuilder builder);
	
}
