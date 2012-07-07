/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.io.Serializable;

import name.martingeisse.admin.navigation.leaf.INavigationLeafVisitor;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;

/**
 * Base interface for all navigation nodes.
 * 
 * TODO: This class should not implement {@link Serializable}. Instead,
 * the {@link NavigationTree} should keep an index of all nodes and
 * pages containing links generated from nodes should refer to the
 * nodes using an index number model.
 */
public interface INavigationNode extends Serializable {

	/**
	 * Getter method for the parent.
	 * @return the parent
	 */
	public INavigationParentNode getParent();

	/**
	 * Setter method for the parent. NOTE: This method is invoked by the parent
	 * when this node is added as a child. Do not otherwise invoke this method.
	 * @param parent the parent to set
	 */
	public void setParent(final INavigationParentNode parent);

	/**
	 * Getter method for the title.
	 * @return the title
	 */
	public String getTitle();
	
	/**
	 * Setter method for the title.
	 * @param title the title to set
	 */
	public void setTitle(final String title);
	
	/**
	 * Checks if this node is a descendant of the specified other node.
	 * This is a strict-descendant check, i.e. returns false if the other
	 * node is the same as this node.
	 * 
	 * Implementation note: This method must not invoke other.isEqualOrAncestorOf(this),
	 * other.isStrictAncestorOf(this) nor this.isEqualOrDescendantOf(other) to avoid
	 * infinite recursion.
	 * 
	 * @param other the other node
	 * @return true if this node is a descendant of the other node, false if not
	 */
	public boolean isStrictDescendantOf(INavigationNode other);

	/**
	 * Checks if this node is the same as the specified other node or is a
	 * descendant of it. 
	 * 
	 * Implementation note: This method is allowed to invoke this.isStrictDescendantOf(other)
	 * without causing infinite recursion. It must not invoke either
	 * other.isEqualOrAncestorOf(this) nor other.isStrictAncestorOf(this)
	 * to avoid infinite recursion.
	 * 
	 * @param other the other node
	 * @return true if this node is equal to or a descendant of the other node, false if not
	 */
	public boolean isEqualOrDescendantOf(INavigationNode other);

	/**
	 * Checks if this node is an ancestor of the specified other node.
	 * This is a strict-ancestor check, i.e. returns false if the other
	 * node is the same as this node. 
	 * 
	 * Implementation note: This method is allowed to invoke other.isEqualOrDescendantOf(this)
	 * and other.isStrictDescendantOf(this) without causing infinite recursion. It must
	 * not invoke this.isEqualOrAncestorOf(other) to avoid infinite recursion.
	 * 
	 * @param other the other node
	 * @return true if this node is a ancestor of the other node, false if not
	 */
	public boolean isStrictAncestorOf(INavigationNode other);

	/**
	 * Checks if this node is the same as the specified other node or is an
	 * ancestor of it. 
	 * 
	 * Implementation note: This method is allowed to invoke this.isStrictAncestorOf(other),
	 * other.isEqualOrDescendantOf(this) and other.isStrictDescendantOf(this) without
	 * causing infinite recursion.
	 * 
	 * @param other the other node
	 * @return true if this node is equal to or a ancestor of the other node, false if not
	 */
	public boolean isEqualOrAncestorOf(INavigationNode other);

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
	public AbstractLink createLink(String id);
	
	/**
	 * Loops through all nodes and applies the specified visitor to
	 * all leaf nodes.
	 * @param visitor the visitor to apply
	 */
	public void visitLeafNodes(INavigationLeafVisitor visitor);
	
}
