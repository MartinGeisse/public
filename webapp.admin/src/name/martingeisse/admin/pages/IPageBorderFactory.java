/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import java.io.Serializable;

import org.apache.wicket.markup.html.border.Border;

/**
 * This factory can be used globally to create application-specific
 * borders around each page, e.g. to add a common header or footer.
 */
public interface IPageBorderFactory extends Serializable {

	/**
	 * Creates a page border.
	 * @param id the wicket id of the border
	 * @return the border
	 */
	public Border createPageBorder(String id);
	
}
