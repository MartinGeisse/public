/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.pagebar;

import name.martingeisse.admin.navigation.IPageBarFactory;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * This factory returns the basic page bars.
 */
public class BasicPageBarFactory implements IPageBarFactory {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.pagebar.IPageBarFactory#createPageTopBar(java.lang.String)
	 */
	@Override
	public Panel createPageTopBar(String id) {
		return new BasicPageTopBar(id);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.component.pagebar.IPageBarFactory#createPageBottomBar(java.lang.String)
	 */
	@Override
	public Panel createPageBottomBar(String id) {
		return new BasicPageBottomBar(id);
	}
	
}
