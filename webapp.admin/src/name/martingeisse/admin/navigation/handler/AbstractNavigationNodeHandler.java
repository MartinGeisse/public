/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.INavigationNodeHandler;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Base implementation for {@link INavigationNodeHandler}.
 */
public abstract class AbstractNavigationNodeHandler implements INavigationNodeHandler {

	/**
	 * the id
	 */
	private String id;

	/**
	 * the title
	 */
	private String title;

	/**
	 * Constructor.
	 */
	public AbstractNavigationNodeHandler() {
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter method for the id.
	 * @param id the id to set
	 * @return this for chaining
	 */
	public AbstractNavigationNodeHandler setId(final String id) {
		this.id = id;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#getId(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getId(NavigationNode node) {
		return getId();
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
	 * @return this for chaining
	 */
	public AbstractNavigationNodeHandler setTitle(final String title) {
		this.title = title;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#getTitle(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public String getTitle(NavigationNode node) {
		return title;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public void mountRequestMappers(AdminWicketApplication application, NavigationNode node) {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createPageBorder()
	 */
	@Override
	public WebMarkupContainer createPageBorder() {
		return null;
	}
	
}
