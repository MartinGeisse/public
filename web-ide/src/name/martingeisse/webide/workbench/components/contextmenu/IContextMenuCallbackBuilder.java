/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components.contextmenu;

import name.martingeisse.webide.workbench.components.SelectableElementsBehavior;

/**
 * This interface is implemented by code that provides an anchor
 * for context menus, such as {@link SelectableElementsBehavior}.
 * It provides the context menu with the ability to send callback
 * requests to the server in whatever way the anchor code deems
 * appropriate.
 * 
 * Technically, this interface provides the ability to implement
 * a client-side callback function that, given a menu item key,
 * triggers a server-side callback that invokes the appropriate
 * menu item (using said key) and passing an appropriate context
 * menu anchor object.
 * 
 * This last point -- passing the anchor object -- is why context
 * menus cannot simply be implemented as a Wicket behavior and
 * provide a callback URL (i.e. a request listener) themselves:
 * They wouldn't have access to the anchor object (e.g. the
 * actual selection from a {@link SelectableElementsBehavior}
 * that way.
 */
public interface IContextMenuCallbackBuilder {

	/**
	 * Writes the method body of the callback method (as defined by
	 * the jQuery context menu plugin) to the specified string builder.
	 * The generated code has access to the menu item key in the
	 * variable "key".
	 * 
	 * @param builder the string builder
	 */
	public void buildContextMenuCallback(StringBuilder builder);
	
}
