/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.pagebar;


import org.apache.wicket.markup.html.panel.Panel;

/**
 * Implementations are able to create top-bars and bottom-bars
 * for admin pages. This interface is implemented by typical
 * contributors for page bars, such as navigation nodes.
 */
public interface IPageBarFactory {

	/**
	 * Creates a top bar for use in a page, or returns null if this factory
	 * does not provide a top bar.
	 * @param id the wicket id
	 * @return the top bar or null
	 */
	public Panel createPageTopBar(String id);

	/**
	 * Creates a bottom bar for use in a page, or returns null if this factory
	 * does not provide a bottom bar.
	 * @param id the wicket id
	 * @return the bottom bar or null
	 */
	public Panel createPageBottomBar(String id);
	
}
