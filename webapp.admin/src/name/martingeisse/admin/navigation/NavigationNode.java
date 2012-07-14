/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler;
import name.martingeisse.admin.pages.GlobalNavigationFolderPage;
import name.martingeisse.common.util.SpecialHandlingList;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class is used for the backbone of the navigation tree.
 * 
 * Navigation nodes have a parent node that is set automatically when
 * the node is added to a parent node.
 * 
 * Navigation nodes also have an ID (returned from its handler) that
 * must be unique among its siblings and must not contain slashes. A
 * node has an implicit path that is constructed from its ID and the
 * IDs of its ancestors like this:
 * 
 * 		/grandparentId/parentId/myId
 * 
 * The node ID can be either:
 * - a string consisting of letters, digits, underscores and dashes.
 *   This generates a fixed segment in the URL.
 * - a variable declaration like this: ${varname}
 *   This generates a variable segment in the URL.
 *   
 * Variable declarations in navigation paths intentionally look
 * like parameter declarations in Wicket mount paths since they are
 * closely related. A variable node generates a mount path with
 * a parameter declaration, thus the corresponding URL segment
 * will be available in the {@link PageParameters}. Variable names
 * can contain letters, digits and underscores.
 * 
 * Each node can have at most one child whose id is a variable declaration.
 * This node has lowest precedence when resolving an URL, which
 * has the effect that the IDs of all its sibling nodes are
 * impossible variable values.
 * 
 * TODO: This class should not implement {@link Serializable}. Instead,
 * the {@link NavigationNode} should keep an index of all nodes and
 * pages containing links generated from nodes should refer to the
 * nodes using an index number model.
 */
public final class NavigationNode implements Iterable<NavigationNode>, Serializable {

	/**
	 * the regularIdPattern
	 */
	private static Pattern regularIdPattern = Pattern.compile("[a-zA-Z0-9\\-\\_]+");

	/**
	 * the variableDeclarationPattern
	 */
	private static Pattern variableDeclarationPattern = Pattern.compile("\\$\\{[a-zA-Z0-9\\_]+\\}");
	
	/**
	 * the parent
	 */
	private NavigationNode parent;

	/**
	 * the children
	 */
	private final List<NavigationNode> children = new SubNodeList();
	
	/**
	 * the handler
	 */
	private INavigationNodeHandler handler;

	/**
	 * Constructor.
	 */
	public NavigationNode() {
	}
	
	/**
	 * Getter method for the parent.
	 * @return the parent
	 */
	public NavigationNode getParent() {
		return parent;
	}
	
	/**
	 * Setter method for the parent. This method is automatically invoked by
	 * the parent when this node is added as a child.
	 * @param parent the parent to set
	 */
	void setParent(NavigationNode parent) {
		this.parent = parent;
	}
	
	/**
	 * Getter method for the children.
	 * @return the children
	 */
	public List<NavigationNode> getChildren() {
		return children;
	}
	
	/**
	 * Getter method for the handler.
	 * @return the handler
	 */
	public INavigationNodeHandler getHandler() {
		return handler;
	}
	
	/**
	 * Setter method for the handler.
	 * @param handler the handler to set
	 */
	public void setHandler(INavigationNodeHandler handler) {
		this.handler = handler;
	}

	/**
	 * Getter method for the id of the handler.
	 * @return the id
	 */
	public String getId() {
		return handler.getId(this);
	}
	
	/**
	 * Getter method for the title of the handler.
	 * @return the title
	 */
	public String getTitle() {
		return handler.getTitle(this);
	}
	
	/**
	 * Checks whether this node is a regular node, i.e. not a variable declaration.
	 * @return true if regular, false if variable declaration or invalid
	 */
	public boolean isRegularNode() {
		return (getId() != null) && regularIdPattern.matcher(getId()).matches();
	}

	/**
	 * Checks if this node is a variable declaration.
	 * @return true if variable declaration, false if regular or invalid
	 */
	public boolean isVariableNode() {
		return (getId() != null) && variableDeclarationPattern.matcher(getId()).matches();
	}
	
	/**
	 * 
	 */
	void initializeTree() {
		
		Set<String> childIds = new HashSet<String>();
		boolean hasVariableChild = false;
		for (NavigationNode child : children) {
			child.initializeTree();
			
			// ensure that the child is either regular or a variable declaration and that no two variables are declared
			if (child.isVariableNode()) {
				if (hasVariableChild) {
					throw new IllegalStateException("navigation node " + getPath() + " has more than one variable child");
				} else {
					hasVariableChild = true;
				}
			} else if (!child.isRegularNode()) {
				throw new IllegalStateException("navigation node ID " + child.getId() + " (parent: " + getPath() + ") is neither a regular nor a variable child (i.e. it has an invalid id)");
			}
			
			// ensure that regular IDs aren't used twice
			if (!childIds.add(child.getId())) {
				throw new IllegalStateException("navigation path " + child.getPath() + " is used twice");
			}
			
		}
	}
	
	/**
	 * 
	 */
	private static void buildPathDescriptionForError(StringBuilder builder, NavigationNode node) {
		if (node.getParent() == null) {
			builder.append('/');
		} else {
			buildPathDescriptionForError(builder, node.getParent());
			builder.append('/');
			INavigationNodeHandler handler = node.getHandler();
			builder.append(handler == null ? "???" : handler.getId(node));
		}
	}
	
	/**
	 * Throws an {@link IllegalStateException} if no handler is set for this node.
	 */
	private void ensureHandlerPresent() {
		if (handler == null) {
			StringBuilder builder = new StringBuilder();
			buildPathDescriptionForError(builder, this);
			throw new IllegalStateException("no handler set; path = " + builder);
		}
	}
	
	/**
	 * Obtains the path of this node. The path is determined from the ID of this
	 * node and the IDs of its ancestors. This requires the node to be placed
	 * in the hierarchy before the path can be obtained. This method assumes
	 * this to be the case and assumes the ancestor without a parent to be
	 * the root, so for disconnected subtrees it will return the path as if
	 * that subtree was a proper tree.
	 * @return the path
	 */
	public final String getPath() {
		ensureHandlerPresent();
		if (parent == null) {
			return "/";
		} else if (parent.getParent() == null) {
			return "/" + handler.getId(this);
		} else {
			return parent.getPath() + "/" + handler.getId(this);
		}
	}
	
	/**
	 * Returns true if this node or any of its ancestors is a variable node.
	 * The check for the root node is omitted -- it is always considered
	 * a regular node. (TODO: actually we should not implicitly assume that
	 * the root is always regular, but rather *enforce* that it is).
	 * 
	 * The return value of this method has a simple meaning: Nodes without
	 * a variable in their path are accessible using a single fixed URL
	 * and are thus natural candidates for global navigation. 
	 * 
	 * @return true if there is a variable in the path, false if not
	 */
	public boolean hasVariablePath() {
		ensureHandlerPresent();
		return (parent != null && (isVariableNode() || parent.hasVariablePath()));
	}
	
	/**
	 * Returns the most specific node for the specified path.
	 * This follows the path into descendant nodes until the
	 * path is completed or a path segment cannot be found.
	 * 
	 * @param path the path to follow
	 * @return the most specific node
	 */
	public NavigationNode findMostSpecificNode(String path) {
		if (path == null || path.isEmpty() || path.charAt(0) != '/') {
			return this;
		}
		int index = path.indexOf('/', 1);
		index = (index == -1 ? path.length() : index);
		String segment = path.substring(1, index);
		for (final NavigationNode child : children) {
			if (segment.equals(child.getId())) {
				return child.findMostSpecificNode(path.substring(index));
			}
		}
		return this;
	}

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
	public final boolean isStrictDescendantOf(final NavigationNode other) {
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
	 * 
	 * Implementation note: This method is allowed to invoke this.isStrictDescendantOf(other)
	 * without causing infinite recursion. It must not invoke either
	 * other.isEqualOrAncestorOf(this) nor other.isStrictAncestorOf(this)
	 * to avoid infinite recursion.
	 * 
	 * @param other the other node
	 * @return true if this node is equal to or a descendant of the other node, false if not
	 */
	public final boolean isEqualOrDescendantOf(final NavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return (this == other || isStrictDescendantOf(other));
	}

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
	public final boolean isStrictAncestorOf(final NavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return other.isStrictDescendantOf(this);
	}

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
	public final boolean isEqualOrAncestorOf(final NavigationNode other) {
		if (other == null) {
			throw new IllegalArgumentException("'other' argument is null");
		}
		return other.isEqualOrDescendantOf(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<NavigationNode> iterator() {
		return children.iterator();
	}

	/**
	 * This method delegates to {@link INavigationNodeHandler#createLink(String, NavigationNode)}.
	 * @param id the wicket id
	 * @return the link or null
	 */
	public AbstractLink createLink(String id) {
		return handler.createLink(id, this);
	}
	
	/**
	 * Loops through all nodes and applies the specified visitor to
	 * all leaf nodes.
	 * @param visitor the visitor to apply
	 */
	public void acceptVisitor(INavigationNodeVisitor visitor) {
		visitor.visit(this);
		for (NavigationNode child : children) {
			child.acceptVisitor(visitor);
		}
	}

	/**
	 * Invokes {@link INavigationNodeHandler#mountRequestMappers(AdminWicketApplication, NavigationNode)}
	 * for all nodes of the whole subtree except for the absolute root node (this node has
	 * no parent). This method is called by the framework.
	 * @param application the wicket application
	 */
	public void mountRequestMappers(AdminWicketApplication application) {
		if (getParent() != null) {
			handler.mountRequestMappers(application, this);
		}
		for (NavigationNode child : children) {
			child.mountRequestMappers(application);
		}
	}
	
	/**
	 * Creates a new {@link NavigationNode} and adds it as a child node.
	 * @return the new node
	 */
	public NavigationNode createChild() {
		NavigationNode child = new NavigationNode();
		getChildren().add(child);
		return child;
	}
	
	/**
	 * Creates a new {@link NavigationNode} with the specified handler and adds
	 * it as a child node.
	 * @param handler the handler
	 * @return the new node
	 */
	public NavigationNode createChild(INavigationNodeHandler handler) {
		NavigationNode child = new NavigationNode();
		child.setHandler(handler);
		getChildren().add(child);
		return child;
	}

	/**
	 * Creates a new {@link NavigationNode} for the default 
	 * {@link GlobalNavigationFolderPage} and adds it as a child node.
	 * @param id the node id of the child
	 * @param title the title of the child
	 * @return the new node
	 */
	public NavigationNode createGlobalNavigationFolderChild(String id, String title) {
		return createChild(new BookmarkablePageNavigationHandler(GlobalNavigationFolderPage.class).setId(id).setTitle(title));
	}
	
	/**
	 * 
	 */
	public void dumpTree() {
		System.out.println("--- begin NavigationNode, id: " + getId() + ", title: " + getTitle() + ", handler: " + handler);
		for (NavigationNode child : children) {
			child.dumpTree();
		}
		System.out.println("--- end NavigationNode");
	}
	
	/**
	 * Specialized list implementation that sets the parent node of sub-nodes.
	 */
	private class SubNodeList extends SpecialHandlingList<NavigationNode> {

		/* (non-Javadoc)
		 * @see name.martingeisse.common.util.SpecialHandlingList#onAfterAddElement(java.lang.Object)
		 */
		@Override
		protected void onAfterAddElement(final NavigationNode element) {
			element.setParent(NavigationNode.this);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.common.util.SpecialHandlingList#onBeforeRemoveElement(java.lang.Object)
		 */
		@Override
		protected void onBeforeRemoveElement(final NavigationNode element) {
			element.setParent(null);
		}

	}

}
