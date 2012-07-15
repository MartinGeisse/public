/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.model;

import name.martingeisse.admin.navigation.NavigationConfigurationUtil;
import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * This model wraps a string-typed model and fetches the
 * navigation node with that path.
 */
public class NavigationNodeModel extends AbstractReadOnlyTransformationModel<NavigationNode, String> {

	/**
	 * Constructor.
	 * @param navigationPath the navigation path
	 */
	public NavigationNodeModel(String navigationPath) {
		super(Model.of(navigationPath));
	}

	/**
	 * Constructor.
	 * @param navigationPathModel the model for the navigation path
	 */
	public NavigationNodeModel(IModel<String> navigationPathModel) {
		super(navigationPathModel);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel#transformValue(java.lang.Object)
	 */
	@Override
	protected NavigationNode transformValue(String wrappedModelValue) {
		return NavigationConfigurationUtil.getNavigationTree().getNodesByPath().get(wrappedModelValue);
	}
	
}
