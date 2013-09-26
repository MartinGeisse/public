/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.IEntityNameAware;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admon.entity.IGetEntityId;
import name.martingeisse.admon.navigation.NavigationNode;
import name.martingeisse.admon.navigation.handlers.BookmarkablePageNavigationHandler;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.RequestHandlerStack.ReplaceHandlerException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * This handler is intended for use within entity-local
 * navigation trees, and especially to build generic
 * nodes for multiple entities. This handler stores the
 * entity name, so mounted page can obtain the node and then
 * the entity name. The entity name is set automatically
 * by the framework when this handler is mounted within
 * entity-local navigation.
 * 
 * Note: The entity name is not directly stored as a page
 * parameter since only the navigation path is secured against
 * manipulation of the URL by the user.
 * 
 * This handler expects to obtain an entity ID. This can be done
 * either by having the page implement {@link IGetEntityId}, or
 * (as a fallback) by taking a page parameter called "entityId".
 */
public class BookmarkableEntityInstanceNavigationHandler extends BookmarkablePageNavigationHandler implements IEntityNameAware {

	/**
	 * the entityName
	 */
	private String entityName;

	/**
	 * Constructor.
	 * @param pageClass the page class
	 */
	public BookmarkableEntityInstanceNavigationHandler(final Class<? extends WebPage> pageClass) {
		super(pageClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameAware#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return entityName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameAware#setEntityName(java.lang.String)
	 */
	@Override
	public void setEntityName(final String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Returns the entity for the entity name stored in this handler.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return ApplicationSchema.instance.findOptionalEntity(getEntityName());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler#prepareMount(name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	protected void prepareMount(final NavigationNode node) {
		super.prepareMount(node);
		if (getEntity() == null) {
			throw new IllegalStateException("unknown entity in navigation node " + node.getPath() + ": " + getEntityName());
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(final String id, final NavigationNode node) {
		return new MyLink(id, getPageClass(), new PageParameters(getExplicitPageParameters()));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.BookmarkablePageNavigationHandler#createReplaceHandlerException(name.martingeisse.admin.navigation.NavigationNode, org.apache.wicket.Component)
	 */
	@Override
	public ReplaceHandlerException createReplaceHandlerException(NavigationNode node, Component context) {
		PageParameters pageParameters = new PageParameters(getExplicitPageParameters());
		pageParameters.add("id", determineEntityId(context));
		return new RestartResponseException(getPageClass(), pageParameters);
	}

	/**
	 * Determines the entity ID to which this navigation node refers in the specified context.
	 * Throws an {@link IllegalStateException} if this method cannot determine the ID.
	 * 
	 * This method is used by {{@link #createReplaceHandlerException(NavigationNode, Component)}
	 * in any case, and by the link from {{@link #createLink(String, NavigationNode)} if
	 * no ID was set in the link explicitly. This allows to have entity-instance links and redirects
	 * created from the navigation nodes (which are not actually instance-local objects) which
	 * automatically resolve their ID. Alternatively, the calling code may use createLink() and add
	 * the "id" parameter manually to specify an explicit ID.
	 * 
	 * @param context the context component
	 * @return the entity ID
	 */
	static Object determineEntityId(Component context) {
		final Page page = context.getPage();
		if (page instanceof IGetEntityId) {
			return ((IGetEntityId)page).getEntityId();
		} else {
			final StringValue parameter = page.getPageParameters().get("id");
			if (parameter == null || parameter.toString() == null) {
				throw new IllegalStateException("BookmarkableEntityInstanceNavigationHandler: the context Page doesn't implement IGetEntityId (page class " + page.getClass().getCanonicalName() + ") and doesn't have an id page parameter either; pageParameters: " + page.getPageParameters());
			}
			return parameter.toString();
		}
	}
	
	/**
	 * The link class must be static because the link must be serializable but the
	 * handler isn't, so we define it here and not as an inner class.
	 */
	private static class MyLink extends BookmarkablePageLink<Void> {

		/**
		 * Constructor.
		 */
		MyLink(final String id, final Class<? extends Page> pageClass, final PageParameters parameters) {
			super(id, pageClass, parameters);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();
			if (!getPageParameters().getNamedKeys().contains("id")) {
				getPageParameters().add("id", determineEntityId(this));
			}
		}

	}
}
