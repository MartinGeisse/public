/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.util.wicket.Constants;
import name.martingeisse.admin.util.wicket.IPageBorderFactory;

import org.apache.wicket.markup.html.border.Border;

/**
 * TODO: document me
 *
 */
public class PageBorderFactory implements IPageBorderFactory {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IPageBorderFactory#createPageBorder(java.lang.String)
	 */
	@Override
	public Border createPageBorder() {
		return new PageBorder(Constants.PAGE_BORDER_ID);
	}

}
