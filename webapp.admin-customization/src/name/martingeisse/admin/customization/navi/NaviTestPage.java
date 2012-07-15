/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.navi;

import name.martingeisse.admin.navigation.component.NavigationMenuPanel;
import name.martingeisse.admin.navigation.component.NavigationMenuView;
import name.martingeisse.admin.navigation.model.NavigationNodeChildrenModel;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * TODO: document me
 *
 */
public class NaviTestPage extends WebPage {

	/**
	 * Constructor.
	 * @param parameters ...
	 */
	public NaviTestPage(final PageParameters parameters) {
		super(parameters);
		add(new NavigationMenuView("navi", NavigationNodeChildrenModel.forParentPath("/"), 2));
		add(new NavigationMenuPanel("naviPanel", NavigationNodeChildrenModel.forParentPath("/"), 0));
	}

}
