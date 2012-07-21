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
import name.martingeisse.admin.component.pageborder.IPageBorderFactory;
import name.martingeisse.admin.navigation.component.NavigationFolderPage;
import name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler;
import name.martingeisse.common.util.SpecialHandlingList;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class is used for the backbone of the navigation tree.
 * 
 * Navigation nodes have a parent node that is set automatically when
 * the node is added to a parent node.
 * 
 * Navigation nodes also have an ID (returned from its handler) that
 * must be unique among its siblings. A node has an implicit path that
 * is constructed from its ID and the IDs of its ancestors like this:
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
 * includes a page border factory. 
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
	 * the handler
	 */
	private INavigationNodeHandler handler;

	/**
	 * the pageBorderFactory
	 */
	private IPageBorderFactory pageBorderFactory;

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
	 * Getter method for the pageBorderFactory.
	 * @return the pageBorderFactory
	 */
	public IPageBorderFactory getPageBorderFactory() {
		return pageBorderFactory;
	}

	/**
	 * Setter method for the pageBorderFactory.
	 * @param pageBorderFactory the pageBorderFactory to set
	 */
	public void setPageBorderFactory(final IPageBorderFactory pageBorderFactory) {
		this.pageBorderFactory = pageBorderFactory;
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
	 * 
	 */
	private static void buildPathDescriptionForError(final StringBuilder builder, final NavigationNode node) {
		if (node.getParent() == null) {
			builder.append('/');
		} else {
			buildPathDescriptionForError(builder, node.getParent());
			builder.append('/');
			final INavigationNodeHandler handler = node.getHandler();
			builder.append(handler == null ? "???" : handler.getId(node));
		}
	}

	/**
	 * Throws an {@link IllegalStateException} if no handler is set for this node.
	 */
	private void ensureHandlerPresent() {
		if (handler == null) {
			final StringBuilder builder = new StringBuilder();
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
	 * Creates a new {@link NavigationNode} and adds it as a child node.
	 * @return the new node
	 */
	public NavigationNode createChild() {
		final NavigationNode child = new NavigationNode();
		getChildren().add(child);
		return child;
	}

	/**
	 * Creates a new {@link NavigationNode} with the specified handler and adds
	 * it as a child node.
	 * @param handler the handler
	 * @return the new node
	 */
	public NavigationNode createChild(final INavigationNodeHandler handler) {
		final NavigationNode child = new NavigationNode();
		child.setHandler(handler);
		getChildren().add(child);
		return child;
	}

	/**
	 * Creates a new {@link NavigationNode} for the default 
	 * {@link NavigationFolderPage} and adds it as a child node.
	 * @param id the node id of the child
	 * @param title the title of the child
	 * @return the new node
	 */
	public NavigationNode createGlobalNavigationFolderChild(final String id, final String title) {
		return createChild(new BookmarkablePageNavigationHandler(NavigationFolderPage.class).setId(id).setTitle(title));
	}

	/**
	 * Creates a new {@link NavigationNode} with a {@link BookmarkablePageNavigationHandler} handler
	 * for the specified page and adds it as a child node. 
	 * @param id the node id
	 * @param title the node title
	 * @param pageClass the page class
	 * @return the new node
	 */
	public NavigationNode createPageChild(final String id, final String title, final Class<? extends WebPage> pageClass) {
		return createChild(new BookmarkablePageNavigationHandler(pageClass).setId(id).setTitle(title));
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
	 * Creates page borders for this navigation nodes and adds them to the
	 * specified list, starting with the outmost.
	 * @param pageBorderList the list to add to
	 */
	public void createPageBorders(final List<WebMarkupContainer> pageBorderList) {
		if (parent != null) {
			parent.createPageBorders(pageBorderList);
		}
		if (pageBorderFactory != null) {
			final WebMarkupContainer border1 = pageBorderFactory.createPageBorder();
			if (border1 != null) {
				pageBorderList.add(border1);
			}
		}
		final WebMarkupContainer border2 = handler.createPageBorder();
		if (border2 != null) {
			pageBorderList.add(border2);
		}
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
