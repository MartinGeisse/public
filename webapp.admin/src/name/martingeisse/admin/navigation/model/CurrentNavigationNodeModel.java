/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.model;

import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.admin.navigation.NavigationPageParameterUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.model.ComponentDetachableModel;
import org.apache.wicket.model.IComponentAssignedModel;

/**
 * Helper for a model that returns the current navigation node.
 * 
 * Since a model for the current navigation node needs a component (to obtain the current page)
 * but is often wrapped by other models, we use a trick here: This model is an
 * {@link IComponentAssignedModel} and wraps another model, but simply delegates all calls
 * to getObject() and setObject(). However, it also has an "inner" model that takes the
 * current page from the "outer" model and returns the current navigation node for it.
 * 
 * The expected use pattern for this class is:
 * - create an instance
 * - take the "inner" model that returns the navigation node and wrap it in other models as needed
 * - set the outermost wrapper model as the delegate model for the instance of this class
 * - pass the instance of this class to a component
 */
public class CurrentNavigationNodeModel extends ComponentDetachableModel<NavigationNode> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.ComponentDetachableModel#getObject(org.apache.wicket.Component)
	 */
	@Override
	protected NavigationNode getObject(Component component) {
		return getCurrentNavigationNode(component);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.ComponentDetachableModel#setObject(org.apache.wicket.Component, java.lang.Object)
	 */
	@Override
	protected void setObject(Component component, NavigationNode object) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the current navigation node for the specified component.
	 * @param component the component
	 * @return the navigation node
	 */
	public static NavigationNode getCurrentNavigationNode(Component component) {
		return getCurrentNavigationNode(component.getPage());
	}

	/**
	 * Returns the current navigation node for the specified page.
	 * @param page the page
	 * @return the navigation node
	 */
	public static NavigationNode getCurrentNavigationNode(Page page) {
		String currentNavigationPath = StringUtils.defaultString(NavigationPageParameterUtil.getNavigationPathForPage(page));
		return NavigationConfigurationUtil.getNavigationTree().getRoot().findMostSpecificNode(currentNavigationPath);
	}
	
}
