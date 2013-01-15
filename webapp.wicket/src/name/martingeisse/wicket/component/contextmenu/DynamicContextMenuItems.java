/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

/**
 * This object groups multiple user-visible menu items as a
 * single {@link ContextMenuItem}. The items are generated
 * dynamically when the context menu is rendered.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public abstract class DynamicContextMenuItems<A> extends ContextMenuItem<A> {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#getReplacementItems()
	 */
	@Override
	protected abstract ContextMenuItem<? super A>[] getReplacementItems();

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object, java.lang.Object)
	 */
	@Override
	final void notifySelectedInternal(A anchor, Object data) {
		throw new UnsupportedOperationException("this item should not actually be used; maybe getReplacementItems() returned null...");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder, name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder)
	 */
	@Override
	final void buildItem(StringBuilder builder, IContextMenuCallbackBuilder callbackBuilder) {
		throw new UnsupportedOperationException("this item should not actually be used; maybe getReplacementItems() returned null...");
	}
	
}
