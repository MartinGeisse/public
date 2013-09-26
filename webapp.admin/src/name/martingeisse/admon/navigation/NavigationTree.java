/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.navigation;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.component.NavigationFolderPage;
import name.martingeisse.admon.navigation.handlers.BookmarkablePageNavigationHandler;

/**
 * This class wraps a tree of navigation nodes. It always contains
 * at least the root node of the tree, and the root node is always
 * a folder node.
 */
public final class NavigationTree {

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
		root.setId(NavigationNode.ROOT_NODE_ID);
		root.setTitle("Home");
		root.setHandler(new BookmarkablePageNavigationHandler(NavigationFolderPage.class));
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
	 * Prepares various internal data structures. This method should be called
	 * after all nodes have been added, but before the tree is used, and before
	 * its request mappers are mounted.
	 */
	public void prepare() {

		// initialize tree structure
		root.validateTree();
		if (!NavigationNode.ROOT_NODE_ID.equals(root.getId())) {
			throw new IllegalStateException("Navigation root node has invalid ID: " + root.getId());
		}

		// cache nodes by full path for quick access
		this.nodesByPath = new HashMap<String, NavigationNode>();
		nodesByPath.put("/", root);
		initializeNodesByPath(root, "/");
		
		// make sure that the "special" URLs used by the framework aren't occupied
		if (nodesByPath.get("/login") != null) {
			throw new IllegalStateException("navigation path /login must not be used by any navigation node");
		}

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
	 * Mounts the request mappers for all navigation nodes.
	 * @param application the wicket application
	 */
	public void mountRequestMappers(final AdminWicketApplication application) {
		root.mountRequestMappers(application);
	}

	/**
	 * This method delegates to root.acceptVisitor(visitor).
	 * @param visitor the visitor
	 */
	public void acceptVisior(final INavigationNodeVisitor visitor) {
		root.acceptVisitor(visitor);
	}

}
