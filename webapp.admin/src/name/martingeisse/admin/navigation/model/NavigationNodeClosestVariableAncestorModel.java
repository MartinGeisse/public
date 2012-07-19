/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.model;

import name.martingeisse.admin.navigation.NavigationNode;
import name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel;

import org.apache.wicket.model.IModel;

/**
 * This model wraps another model of type {@link NavigationNode} and returns the
 * closest ancestor node of the model value of the wrapped model that is a
 * variable node. Returns null if no such ancestor exists or if the wrapped
 * model had returned null.
 */
public class NavigationNodeClosestVariableAncestorModel extends AbstractReadOnlyTransformationModel<NavigationNode, NavigationNode> {

	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 */
	public NavigationNodeClosestVariableAncestorModel(IModel<NavigationNode> wrappedModel) {
		super(wrappedModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel#transformValue(java.lang.Object)
	 */
	@Override
	protected NavigationNode transformValue(NavigationNode node) {
		while (node != null) {
			if (node.isVariableNode()) {
				return node;
			}
			node = node.getParent();
		}
		return null;
	}
	
}
