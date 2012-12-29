/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components.contextmenu;

/**
 * A pseudo-item that draws a separator line in a context menu.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public final class ContextMenuSeparator<A> extends ContextMenuItem<A> {

	/**
	 * Constructor.
	 */
	public ContextMenuSeparator() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object)
	 */
	@Override
	final void notifySelectedInternal(A anchor, Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder, name.martingeisse.webide.workbench.components.contextmenu.IContextMenuCallbackBuilder)
	 */
	@Override
	void buildItem(StringBuilder builder, IContextMenuCallbackBuilder callbackBuilder) {
		builder.append("'---'");
	}

}
