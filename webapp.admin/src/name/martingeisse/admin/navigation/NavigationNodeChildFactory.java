/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.navigation.component.NavigationFolderPage;
import name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler;
import name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;
import name.martingeisse.admin.navigation.handler.EntityListPanelHandler;
import name.martingeisse.admin.navigation.handler.FirstChildNavigationHandler;
import name.martingeisse.admin.navigation.handler.UrlNavigationHandler;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.Panel;

import com.mysema.query.types.Predicate;

/**
 * This class contains various methods to create child nodes for navigation
 * nodes. It exists mainly to keep those methods from cluttering
 * the {@link NavigationNode} class.
 */
public class NavigationNodeChildFactory {

	/**
	 * the navigationNode
	 */
	private final NavigationNode navigationNode;

	/**
	 * Constructor.
	 * @param navigationNode the node to create children for
	 */
	public NavigationNodeChildFactory(final NavigationNode navigationNode) {
		this.navigationNode = navigationNode;
	}

	/**
	 * Getter method for the navigationNode.
	 * @return the navigationNode
	 */
	public NavigationNode getNavigationNode() {
		return navigationNode;
	}

	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	// basic factory methods
	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------

	/**
	 * Creates a new {@link NavigationNode} and adds it as a child node.
	 * @return the new node
	 */
	public final NavigationNode createChild() {
		final NavigationNode child = new NavigationNode();
		navigationNode.getChildren().add(child);
		return child;
	}

	/**
	 * Sets the specified id and title for the handler, then creates a new
	 * {@link NavigationNode} with the handler and adds it as a child node.
	 * @param id the node ID to set in the handler
	 * @param title the node title to set in the handler
	 * @param handler the handler
	 * @return the new child node
	 */
	public final NavigationNode createChild(final String id, final String title, final AbstractNavigationNodeHandler handler) {
		final NavigationNode child = new NavigationNode();
		child.setId(id);
		child.setTitle(title);
		child.setHandler(handler);
		navigationNode.getChildren().add(child);
		return child;
	}

	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	// factory methods for generic handlers
	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------

	/**
	 * Creates a new {@link NavigationNode} with a {@link BookmarkablePageNavigationHandler} handler
	 * for the specified page and adds it as a child node. 
	 * @param id the node id
	 * @param title the node title
	 * @param pageClass the page class
	 * @return the new node
	 */
	public final NavigationNode createPageChild(final String id, final String title, final Class<? extends WebPage> pageClass) {
		return createChild(id, title, new BookmarkablePageNavigationHandler(pageClass));
	}

	/**
	 * Creates a new {@link NavigationNode} with a {@link FirstChildNavigationHandler}
	 * and adds it as a child node.
	 * @param id the node id of the child
	 * @param title the title of the child
	 * @return the new node
	 */
	public final NavigationNode createFirstChildHandlerChild(final String id, final String title) {
		return createChild(id, title, new FirstChildNavigationHandler());
	}

	/**
	 * Creates a new {@link NavigationNode} with a {@link FirstChildNavigationHandler}
	 * and adds it as a child node.
	 * @param id the node id of the child
	 * @param title the title of the child
	 * @param url the URL to which the node's link leads
	 * @return the new node
	 */
	public final NavigationNode createUrlChild(final String id, final String title, final String url) {
		return createChild(id, title, new UrlNavigationHandler(url));
	}

	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	// factory methods for generic entity-related pages
	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------

	/**
	 * Creates a new {@link NavigationNode} with a {@link EntityListPanelHandler} handler
	 * for the specified entity-list panel and adds it as a child node. 
	 * @param id the node id
	 * @param title the node title
	 * @param panelClass the panel class
	 * @param entityName the entity name
	 * @return the new node
	 */
	public final NavigationNode createEntityListPanelChild(final String id, final String title, final Class<? extends Panel> panelClass, final String entityName) {
		return createChild(id, title, new EntityListPanelHandler(panelClass, entityName));
	}

	/**
	 * Creates a new {@link NavigationNode} with a {@link EntityListPanelHandler} handler
	 * for the specified entity-list panel and adds it as a child node. 
	 * @param id the node id
	 * @param title the node title
	 * @param panelClass the panel class
	 * @param entityName the entity name
	 * @param filterPredicate the filter predicate used to produce the list
	 * @return the new node
	 */
	public final NavigationNode createEntityListPanelChild(final String id, final String title, final Class<? extends Panel> panelClass, final String entityName, final Predicate filterPredicate) {
		return createChild(id, title, new EntityListPanelHandler(panelClass, entityName).setFilter(filterPredicate));
	}

	/**
	 * Creates a new {@link NavigationNode} with a {@link EntityListPanelHandler} handler
	 * for the specified entity-list panel and adds it as a child node. 
	 * @param id the node id
	 * @param title the node title
	 * @param panelClass the panel class
	 * @param entityName the entity name
	 * @param filter the filter used to produce the list
	 * @return the new node
	 */
	public final NavigationNode createEntityListPanelChild(final String id, final String title, final Class<? extends Panel> panelClass, final String entityName, IEntityListFilter filter) {
		return createChild(id, title, new EntityListPanelHandler(panelClass, entityName).setFilter(filter));
	}

	/**
	 * Creates a new {@link NavigationNode} with a {@link EntityInstancePanelHandler} handler
	 * for the specified entity-instance panel and adds it as a child node. 
	 * @param id the node id
	 * @param title the node title
	 * @param panelClass the panel class
	 * @return the new node
	 */
	public final NavigationNode createEntityInstancePanelChild(final String id, final String title, final Class<? extends Panel> panelClass) {
		return createChild(id, title, new EntityInstancePanelHandler(panelClass));
	}

	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------
	// factory methods for specific handlers
	// ----------------------------------------------------------------------------------
	// ----------------------------------------------------------------------------------

	/**
	 * Creates a new {@link NavigationNode} with a 
	 * {@link NavigationFolderPage} handler and adds it as a child node.
	 * @param id the node id of the child
	 * @param title the title of the child
	 * @return the new node
	 */
	public final NavigationNode createNavigationFolderChild(final String id, final String title) {
		return createChild(id, title, new BookmarkablePageNavigationHandler(NavigationFolderPage.class));
	}

}
