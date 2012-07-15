/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.model;

import java.util.List;

import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel;

import org.apache.wicket.model.IModel;

/**
 * This model wraps a {@link NavigationNode}-typed model and fetches the
 * children of that node.
 */
public class NavigationNodeChildrenModel extends AbstractReadOnlyTransformationModel<List<NavigationNode>, NavigationNode> {

	/**
	 * Constructor.
	 * @param navigationPathModel the model for the navigation path
	 */
	private NavigationNodeChildrenModel(IModel<NavigationNode> navigationPathModel) {
		super(navigationPathModel);
	}
	
	/**
	 * Creates an instance, with the specified model returning the parent node
	 * whose children shall be returned by this model.
	 * 
	 * @param parentModel the model for the parent node
	 * @return the model instance
	 */
	public static NavigationNodeChildrenModel forParentModel(IModel<NavigationNode> parentModel) {
		return new NavigationNodeChildrenModel(parentModel);
	}
	
	/**
	 * Creates an instance, with the specified model returning the path to the
	 * parent node whose children shall be returned by this model.
	 * 
	 * @param parentPathModel the model for the path of the parent node
	 * @return the model instance
	 */
	public static NavigationNodeChildrenModel forParentPathModel(IModel<String> parentPathModel) {
		return new NavigationNodeChildrenModel(new NavigationNodeModel(parentPathModel));
	}
	
	/**
	 * Creates an instance, using the specified path to the
	 * parent node whose children shall be returned by this model.
	 * 
	 * @param parentPath the path of the parent node
	 * @return the model instance
	 */
	public static NavigationNodeChildrenModel forParentPath(String parentPath) {
		return new NavigationNodeChildrenModel(new NavigationNodeModel(parentPath));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel#transformValue(java.lang.Object)
	 */
	@Override
	protected List<NavigationNode> transformValue(NavigationNode wrappedModelValue) {
		return wrappedModelValue.getChildren();
	}

}
