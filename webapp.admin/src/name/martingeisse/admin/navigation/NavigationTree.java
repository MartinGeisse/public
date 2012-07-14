/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.handler.GlobalNavigationFolderHandler;

/**
 * This class wraps a tree of navigation nodes. It always contains
 * at least the root node of the tree, and the root node is always
 * a folder node.
 */
public final class NavigationTree implements Serializable {

	/**
	 * the root
	 */
	private NavigationNode root;

	/**
	 * the nodesByPath
	 */
	private Map<String, NavigationNode> nodesByPath;

	/**
	 * Constructor.
	 */
	public NavigationTree() {
		root = new NavigationNode();
		root.setHandler(new GlobalNavigationFolderHandler().setId("dummy").setTitle("Dummy"));
	}

	/**
	 * Getter method for the root.
	 * @return the root
	 */
	public NavigationNode getRoot() {
		return root;
	}

	/**
	 * Setter method for the root.
	 * @param root the root to set
	 */
	public void setRoot(final NavigationNode root) {
		this.root = root;
	}

	/**
	 * Prepares various internal data structures. This method is called by the
	 * framework after all nodes have been added but before the tree is used.
	 */
	public void prepare() {
		root.initializeTree();
		this.nodesByPath = new HashMap<String, NavigationNode>();
		nodesByPath.put("/", root);
		initializeNodesByPath(root, "/");
	}

	/**
	 * 
	 */
	private void initializeNodesByPath(final Iterable<NavigationNode> nodes, final String prefix) {
		for (final NavigationNode node : nodes) {
			final String path = prefix + node.getId();
			nodesByPath.put(path, node);
			initializeNodesByPath(node, path + "/");
		}
	}

	/**
	 * Getter method for the nodesByPath.
	 * @return the nodesByPath
	 */
	public Map<String, NavigationNode> getNodesByPath() {
		return nodesByPath;
	}

	/**
	 * Mounts the request mappers for all navigation nodes. This method
	 * is called by the framework.
	 * @param application the wicket application
	 */
	public void mountRequestMappers(final AdminWicketApplication application) {
		root.mountRequestMappers(application);
	}

}
