/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

/**
 * This interface allows to separate the menu item class hierarchy
 * from handler code. Implementations of this interface are attached
 * to appropriate menu items, e.g. {@link DelegatingContextMenuItem}
 * or {@link DelegatingContextMenuItemWithTextInput}, which then
 * invoke this interface.
 * 
 * @param <C> the type of context data specified with the menu item
 * @param <A> the menu anchor type
 * @param <P> the type of additional parameter data passed on invocation
 */
public interface IContextMenuDelegate<C, A, P> {

	/**
	 * Invokes this delegate.
	 * 
	 * @param context the context (taken from the menu item)
	 * @param anchor the anchor (selected by the user)
	 * @param parameter additional parameter (specified by the user)
	 */
	public void invoke(C context, A anchor, P parameter);

}
