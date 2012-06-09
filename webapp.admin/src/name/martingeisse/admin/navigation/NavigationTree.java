/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

/**
 * This class wraps a tree of navigation nodes. It always contains
 * at least the root node of the tree, and the root node is always
 * a folder node.
 */
public final class NavigationTree {

	/**
	 * the root
	 */
	private NavigationFolder root;

	/**
	 * Constructor.
	 */
	public NavigationTree() {
		root = new NavigationFolder();
		root.setTitle("Navigation");
	}
	
	/**
	 * Getter method for the root.
	 * @return the root
	 */
	public NavigationFolder getRoot() {
		return root;
	}

	/**
	 * Setter method for the root.
	 * @param root the root to set
	 */
	public void setRoot(final NavigationFolder root) {
		this.root = root;
	}

}
