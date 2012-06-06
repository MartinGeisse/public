/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import org.apache.wicket.markup.html.border.Border;

import name.martingeisse.admin.application.capabilities.IPageBorderFactory;

/**
 * TODO: document me
 *
 */
public class PageBorderFactory implements IPageBorderFactory {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IPageBorderFactory#createPageBorder(java.lang.String)
	 */
	@Override
	public Border createPageBorder(String id) {
		return new PageBorder(id);
	}

}
