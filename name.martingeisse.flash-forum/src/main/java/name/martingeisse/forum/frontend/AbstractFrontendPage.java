/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.frontend;

import name.martingeisse.forum.application.page.AbstractApplicationPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

/**
 * Base class for all frontend pages.
 */
public class AbstractFrontendPage extends AbstractApplicationPage {

	/**
	 * Constructor.
	 */
	public AbstractFrontendPage() {
		add(new BookmarkablePageLink<>("impressumLink", ImpressumPage.class));
	}
	
}
