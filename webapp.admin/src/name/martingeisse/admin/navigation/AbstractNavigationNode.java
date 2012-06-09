/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.io.Serializable;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;

/**
 * The base class for all nodes of the navigation tree.
 * 
 * TODO: This class should not implement {@link Serializable}. Instead,
 * the {@link NavigationTree} should keep an index of all nodes and
 * pages containing links generated from nodes should refer to the
 * nodes using an index number model.
 */
public abstract class AbstractNavigationNode implements Serializable {

	/**
	 * the parent
	 */
	private NavigationFolder parent;

	/**
	 * the title
	 */
	private String title;

	/**
	 * Constructor.
	 */
	public AbstractNavigationNode() {
	}

	/**
	 * Getter method for the parent.
	 * @return the parent
	 */
	public NavigationFolder getParent() {
		return parent;
	}

	/**
	 * Setter method for the parent.
	 * @param parent the parent to set
	 */
	public void setParent(final NavigationFolder parent) {
		this.parent = parent;
	}

	/**
	 * Getter method for the title.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for the title.
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Checks if this node is a descendant of the specified other node.
	 * This is a strict-descendant check, i.e. returns false if the other
	 * node is the same as this node. 
	 * @param other the other node
	 * @return true if this node is a descendant of the other node, false if not
	 */
	public final boolean isDescendantOf(AbstractNavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		if (parent == null) {
			return false;
		}
		return parent.isEqualOrDescendantOf(other);
	}

	/**
	 * Checks if this node is the same as the specified other node or is a
	 * descendant of it. 
	 * @param other the other node
	 * @return true if this node is equal to or a descendant of the other node, false if not
	 */
	public final boolean isEqualOrDescendantOf(AbstractNavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return (this == other || isDescendantOf(other));
	}

	/**
	 * Checks if this node is an ancestor of the specified other node.
	 * This is a strict-ancestor check, i.e. returns false if the other
	 * node is the same as this node. 
	 * @param other the other node
	 * @return true if this node is a ancestor of the other node, false if not
	 */
	public final boolean isAncestorOf(AbstractNavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return other.isDescendantOf(this);
	}

	/**
	 * Checks if this node is the same as the specified other node or is a
	 * ancestor of it. 
	 * @param other the other node
	 * @return true if this node is equal to or a ancestor of the other node, false if not
	 */
	public final boolean isEqualOrAncestorOf(AbstractNavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return other.isEqualOrDescendantOf(this);
	}
	
	/**
	 * Creates a Wicket {@link Link} for this node. The link will
	 * refer to the correct URL for this node, but has no child
	 * components or associated markup. That is, the appearance of
	 * the link is up to the caller.
	 * 
	 * This method may return null to indicate that a node cannot be
	 * linked to. This happens for folder nodes if the node presentation
	 * does not support folder pages. It may happen for leaf nodes,
	 * in which case the node should be presented as "disabled".
	 * 
	 * @param id the wicket id
	 * @return the link or null
	 */
	public abstract AbstractLink createLink(String id);

	/**
	 * Loops through all nodes and applies the specified visitor to
	 * all leaf nodes.
	 * @param visitor the visitor to apply
	 */
	public abstract void visitLeafNodes(INavigationLeafVisitor visitor);
	
}
