/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * Getter method interface for {@link IPageable}.
 */
public interface IGetPageable {

	/**
	 * Getter method for the pageable.
	 * @return the pageable
	 */
	public IPageable getPageable();

}
