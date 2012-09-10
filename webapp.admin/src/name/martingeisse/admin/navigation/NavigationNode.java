/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.component.pagebar.IPageBarFactory;
import name.martingeisse.common.util.SpecialHandlingList;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class is used for the backbone of the navigation tree.
 * 
 * Navigation nodes have a parent node that is set automatically when
 * the node is added to a parent node.
 * 
 * Navigation nodes also have an ID that must be unique among its
 * siblings. A node has an implicit path that is constructed from
 * its ID and the IDs of its ancestors like this:
 * 
 * 		/grandparentId/parentId/myId
 * 
 * The node ID can be either:
 * - a string consisting of letters, digits, underscores and dashes.
 *   This generates a fixed segment in the URL (regular node).
 * - a variable declaration like this: ${varname}
 *   This generates a variable segment in the URL (variable node).
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
 * The root node has a regular, fixed ID defined by the ROOT_NODE_ID
 * constant. This ID is not intended to be visible anywhere.
 * 
 * A node can hold additional data besides its handler. Currently, this
 * includes a page bar factory. 
 */
public final class NavigationNode implements Iterable<NavigationNode> {

	/**
	 * The ID of the root node.
	 */
	public static final String ROOT_NODE_ID = "ROOT-NODE-ID";

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
	 * the id
	 */
	private String id;

	/**
	 * the title
	 */
	private String title;

	/**
	 * the handler
	 */
	private INavigationNodeHandler handler;

	/**
	 * the pageBarFactory
	 */
	private IPageBarFactory pageBarFactory;

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
	void setParent(final NavigationNode parent) {
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
	 * Getter method for the id.
	 * @return the id
	 */
	public String getId() {
		return (id == null && handler != null) ? handler.getFallbackId(this) : id;
	}

	/**
	 * Setter method for the id.
	 * @param id the id to set
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Getter method for the title.
	 * @return the title
	 */
	public String getTitle() {
		return (title == null && handler != null) ? handler.getFallbackTitle(this) : title;
	}

	/**
	 * Setter method for the title.
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
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
	public void setHandler(final INavigationNodeHandler handler) {
		this.handler = handler;
	}

	/**
	 * Getter method for the pageBarFactory.
	 * @return the pageBarFactory
	 */
	public IPageBarFactory getPageBarFactory() {
		return pageBarFactory;
	}

	/**
	 * Setter method for the pageBarFactory.
	 * @param pageBarFactory the pageBarFactory to set
	 */
	public void setPageBarFactory(final IPageBarFactory pageBarFactory) {
		this.pageBarFactory = pageBarFactory;
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
	 * Looks for a child with the specified id.
	 * @param id the id to look for
	 * @return the child, or null if none was found
	 */
	public NavigationNode findChildById(final String id) {
		for (final NavigationNode child : children) {
			if (child.getId().equals(id)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	void initializeTree() {

		final Set<String> childIds = new HashSet<String>();
		boolean hasVariableChild = false;
		for (final NavigationNode child : children) {
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
	 * Builds a string that is equal to the path if all IDs are properly set. This method
	 * can handle missing IDs though, and will print them like this:
	 * 
	 * 		/foo/???/bar
	 * 
	 * @param builder the string builder to write to
	 */
	public void buildPathDescriptionForError(final StringBuilder builder) {
		if (getParent() == null) {
			builder.append('/');
		} else {
			getParent().buildPathDescriptionForError(builder);
			builder.append('/');
			final String id = getId();
			builder.append(id == null ? "???" : id);
		}
	}

	/**
	 * Like buildPathDescriptionForError() but returns the string instead of writing it
	 * to a {@link StringBuilder}.
	 * @return the path description
	 */
	public String getPathDescriptionForError() {
		final StringBuilder builder = new StringBuilder();
		buildPathDescriptionForError(builder);
		return builder.toString();
	}

	/**
	 * Throws an {@link IllegalStateException} if no handler is set for this node.
	 */
	public void ensureHandlerPresent() {
		if (handler == null) {
			throw new IllegalStateException("no handler set; path = " + getPathDescriptionForError());
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
		if (parent == null) {
			return "/";
		} else if (parent.getParent() == null) {
			return "/" + getId();
		} else {
			return parent.getPath() + "/" + getId();
		}
	}

	/**
	 * Returns true if this node or any of its ancestors is a variable node.
	 * 
	 * The return value of this method has a simple meaning: Nodes without
	 * a variable in their path are accessible using a single fixed URL
	 * and are thus natural candidates for global navigation. 
	 * 
	 * @return true if there is a variable in the path, false if not
	 */
	public boolean hasVariablePath() {
		return (parent != null && (isVariableNode() || parent.hasVariablePath()));
	}

	/**
	 * Returns the closest ancestor node that is a variable node, or null if no
	 * such ancestor exists.
	 * 
	 * @return the closest variable ancestor or null
	 */
	public NavigationNode getClosestVariableAncestor() {
		if (isVariableNode()) {
			return this;
		}
		if (parent == null) {
			return null;
		}
		return parent.getClosestVariableAncestor();
	}

	/**
	 * Returns the most specific node for the specified path.
	 * This follows the path into descendant nodes until the
	 * path is completed or a path segment cannot be found.
	 * 
	 * @param path the path to follow
	 * @return the most specific node
	 */
	public NavigationNode findMostSpecificNode(final String path) {
		if (path == null || path.isEmpty() || path.charAt(0) != '/') {
			return this;
		}
		int index = path.indexOf('/', 1);
		index = (index == -1 ? path.length() : index);
		final String segment = path.substring(1, index);
		for (final NavigationNode child : children) {
			if (segment.equals(child.getId())) {
				return child.findMostSpecificNode(path.substring(index));
			}
		}
		return this;
	}

	/**
	 * Checks if one of the ancestors of this node (but not this node itself)
	 * has the specified path.
	 * 
	 * @param path the path
	 * @return true if one of the ancestors of this node has the specified path, false if not
	 */
	public final boolean isStrictDescendantOf(final String path) {
		if (path == null) {
			throw new IllegalArgumentException("'path' argument is null");
		}
		if (parent == null) {
			return false;
		}
		return parent.isEqualOrDescendantOf(path);
	}

	/**
	 * Checks if this node or one of its ancestors has the specified path
	 * 
	 * @param path the path
	 * @return true if this node or one of its ancestors has the specified path, false if not
	 */
	public final boolean isEqualOrDescendantOf(final String path) {
		if (path == null) {
			throw new IllegalArgumentException("'path' argument is null");
		}
		return (getPath().equals(path) || isStrictDescendantOf(path));
	}

	/**
	 * Checks if this node is a plausible ancestor of a node with the
	 * specified path. This makes sure that the path of this node
	 * is compatible with the specified path, but not whether such a
	 * descendant node of this node actually exists.
	 * 
	 * This method does a strict ancestor check, i.e. it will return
	 * false if the specified path is the path of this node.
	 * 
	 * @param path the path
	 * @return true if this node is a plausible ancestor of the node with that path, false if not
	 */
	public final boolean isStrictPlausibleAncestorOf(final String path) {
		if (path == null) {
			throw new IllegalArgumentException("'path' argument is null");
		} else if (getParent() == null) {
			return true;
		} else {
			// note: the trailing slash avoids that "/foo/bar" is mistaken
			// as a plausible ancestor of "/foo/barr", and also makes the
			// comparison a strict one.
			return path.startsWith(getPath() + "/");
		}
	}

	/**
	 * Checks if this node has the specified path or is a plausible
	 * ancestor of a node with the specified path. This makes sure
	 * that the path of this node is compatible with the specified
	 * path, but (if the paths are not equal), not check whether
	 * such a descendant node of this node actually exists.
	 * 
	 * @param path the path
	 * @return true if this node has the specified path or is a
	 * plausible ancestor of the node with that path, false if it
	 * has an incompatible path
	 */
	public final boolean isEqualOrPlausibleAncestorOf(final String path) {
		if (path == null) {
			throw new IllegalArgumentException("'path' argument is null");
		}
		return (getPath().equals(path) || isStrictPlausibleAncestorOf(path));
	}

	/**
	 * Checks if this node is a descendant of the specified other node.
	 * This is a strict-descendant check, i.e. returns false if the other
	 * node is the same as this node.
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
	 * @return the link
	 */
	public AbstractLink createLink(final String id) {
		return handler.createLink(id, this);
	}

	/**
	 * This method delegates to {@link INavigationNodeHandler#createReplaceHandlerException(NavigationNode, Component)}.
	 * @param context the context component
	 * @return the link
	 */
	public ReplaceHandlerException createReplaceHandlerException(Component context) {
		return handler.createReplaceHandlerException(this, context);
	}

	/**
	 * Loops through all nodes and applies the specified visitor to
	 * all leaf nodes.
	 * @param visitor the visitor to apply
	 */
	public void acceptVisitor(final INavigationNodeVisitor visitor) {
		visitor.visit(this);
		for (final NavigationNode child : children) {
			child.acceptVisitor(visitor);
		}
	}

	/**
	 * Invokes {@link INavigationNodeHandler#mountRequestMappers(AdminWicketApplication, NavigationNode)}
	 * for all nodes of the whole subtree except for the absolute root node (this node has
	 * no parent). This method is called by the framework.
	 * @param application the wicket application
	 */
	public void mountRequestMappers(final AdminWicketApplication application) {
		if (getParent() != null) {
			handler.mountRequestMappers(application, this);
		}
		for (final NavigationNode child : children) {
			child.mountRequestMappers(application);
		}
	}

	/**
	 * @return a {@link NavigationNodeChildFactory} for this node.
	 */
	public NavigationNodeChildFactory getChildFactory() {
		return new NavigationNodeChildFactory(this);
	}

	/**
	 * 
	 */
	public void dumpTree() {
		System.out.println("--- begin NavigationNode, id: " + getId() + ", title: " + getTitle() + ", handler: " + handler);
		for (final NavigationNode child : children) {
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
