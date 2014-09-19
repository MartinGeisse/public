/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.common.frontend;

import name.martingeisse.slave_services.babel.frontend.OverviewPage;
import name.martingeisse.slave_services.papyros.frontend.family.TemplateFamilyListPage;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;


/**
 * The "home" page.
 */
public class HomePage extends AbstractFrontendPage {
	
	/**
	 * Constructor.
	 */
	public HomePage() {
		add(new BookmarkablePageLink<Void>("papyrosLink", TemplateFamilyListPage.class));
		add(new BookmarkablePageLink<Void>("babelLink", OverviewPage.class));
	}
	
}
