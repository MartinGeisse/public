/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

import java.io.Serializable;

/**
 * Base class for context menu items.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public abstract class ContextMenuItem<A> implements Serializable {

	/**
	 * Constructor.
	 */
	ContextMenuItem() {
	}

	/**
	 * Allows context menu items to replace themselves on-the-fly by any number
	 * of items that are actually used. The default implementation
	 * returns null, causing this item to be used.
	 * 
	 * @return the items to actually use, or null to just use this item
	 */
	protected ContextMenuItem<? super A>[] getReplacementItems() {
		return null;
	}
	
	/**
	 * Triggers the effect of this menu item.
	 * @param anchor the anchor
	 * @param data additional data from the client-side code for this menu item
	 */
	public final void notifySelected(A anchor, Object data) {
		notifySelectedInternal(anchor, data);
	}
	
	/**
	 * This method is invoked when the item is selected.
	 */
	abstract void notifySelectedInternal(A anchor, Object data);

	/**
	 * Internal method used to generate the code for this menu.
	 * @param builder the string builder to append to
	 * @param callbackBuilder an object that knows how to generate callback requests
	 * to the server and route them to the appropriate menu item
	 */
	abstract void buildItem(StringBuilder builder, IContextMenuCallbackBuilder callbackBuilder);
	
}
