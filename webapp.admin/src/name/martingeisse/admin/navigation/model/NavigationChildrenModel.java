/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.model;

import java.util.List;

import name.martingeisse.admin.navigation.NavigationConfiguration;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * This model returns the child nodes of a single fixed node
 * that is specified by its path. This is useful for a navigation
 * menu added to the page bars for that node.
 */
public class NavigationChildrenModel extends LoadableDetachableModel<List<NavigationNode>> {

	/**
	 * the path
	 */
	private String path;

	/**
	 * Constructor.
	 */
	public NavigationChildrenModel() {
	}

	/**
	 * Constructor.
	 * @param path the path of the node
	 */
	public NavigationChildrenModel(final String path) {
		this.path = path;
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Setter method for the path.
	 * @param path the path to set
	 */
	public void setPath(final String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected List<NavigationNode> load() {
		return NavigationConfiguration.navigationTreeParameter.get().getNodesByPath().get(path).getChildren();
	}

}
