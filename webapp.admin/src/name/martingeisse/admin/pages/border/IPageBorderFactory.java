/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages.border;


import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Implementations are able to create page borders. This interface is
 * implemented by typical contributors for page borders, such as
 * navigation nodes.
 */
public interface IPageBorderFactory {

	/**
	 * Creates the page border for this node handler, or returns null if this factory
	 * does not provide a page border. The page border must use {@link PageBorderUtil#PAGE_BORDER_ID}.
	 * @return the border or null
	 */
	public WebMarkupContainer createPageBorder();
	
}
