/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;

/**
 * Base class for context menu items that generate custom markup.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public abstract class AbstractMarkupMenuItem<A> extends ContextMenuItem<A> {

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object, java.lang.Object)
	 */
	@Override
	void notifySelectedInternal(final A anchor, final Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder, name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder)
	 */
	@Override
	void buildItem(final StringBuilder builder, final IContextMenuCallbackBuilder callbackBuilder) {
		builder.append("createContextMenuItemWithCustomMarkup('");
		builder.append(JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(getMarkup()));
		builder.append("')");
	}

	/**
	 * This method generates the markup for the menu item.
	 * @return the markup
	 */
	protected abstract String getMarkup();
	
}
