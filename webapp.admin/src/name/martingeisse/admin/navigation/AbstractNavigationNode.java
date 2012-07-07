/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;


/**
 * The base class for all nodes of the navigation tree.
 */
public abstract class AbstractNavigationNode implements INavigationNode {

	/**
	 * the parent
	 */
	private INavigationParentNode parent;

	/**
	 * the title
	 */
	private String title;

	/**
	 * Constructor.
	 */
	public AbstractNavigationNode() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#getParent()
	 */
	@Override
	public final INavigationParentNode getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#setParent(name.martingeisse.admin.navigation.NavigationFolder)
	 */
	@Override
	public final void setParent(final INavigationParentNode parent) {
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#getTitle()
	 */
	@Override
	public final String getTitle() {
		return title;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#setTitle(java.lang.String)
	 */
	@Override
	public final void setTitle(final String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#isStrictDescendantOf(name.martingeisse.admin.navigation.INavigationNode)
	 */
	@Override
	public final boolean isStrictDescendantOf(INavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		if (parent == null) {
			return false;
		}
		return parent.isEqualOrDescendantOf(other);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#isEqualOrDescendantOf(name.martingeisse.admin.navigation.INavigationNode)
	 */
	@Override
	public final boolean isEqualOrDescendantOf(INavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return (this == other || isStrictDescendantOf(other));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#isStrictAncestorOf(name.martingeisse.admin.navigation.INavigationNode)
	 */
	@Override
	public final boolean isStrictAncestorOf(INavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return other.isStrictDescendantOf(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#isEqualOrAncestorOf(name.martingeisse.admin.navigation.INavigationNode)
	 */
	@Override
	public final boolean isEqualOrAncestorOf(INavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return other.isEqualOrDescendantOf(this);
	}
	
}
