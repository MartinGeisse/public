/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.entity.IEntityNameAware;
import name.martingeisse.admin.entity.IGetEntityId;
import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * This handler is intended for use within entity-local
 * navigation trees, and especially the general template
 * for such trees. When the template is cloned for an entity,
 * this handler receives the entity name and stores it
 * in an implicit page parameter called "entity".
 * 
 * This handler expects to obtain an entity ID. This can be done
 * either by having the page implement {@link IGetEntityId}, or
 * (as a fallback) by taking a page parameter called "entityId".
 */
public class BookmarkableEntityInstanceNavigationHandler extends BookmarkablePageNavigationHandler implements IEntityNameAware {

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
		final StringValue value = getImplicitPageParameters().get("entity");
		return (value == null ? null : value.toString());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.IEntityNameAware#setEntityName(java.lang.String)
	 */
	@Override
	public void setEntityName(final String entityName) {
		// TODO: make sure the URL cannot override this parameter (security!)
		getImplicitPageParameters().remove("entity");
		getImplicitPageParameters().add("entity", entityName);
	}

	/**
	 * Returns the entity for the entity name stored in this handler.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return ApplicationSchema.instance.findEntity(getEntityName());
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

			// Add the entity ID from the current page to the parameters if no "id" parameter exists yet.
			// This allows to have instance-local links created from the navigation nodes (which are not
			// actually instance-local objects) which automatically resolve their ID. At the same time,
			// the calling code may use createLink() and add parameters manually to specify an explicit ID.
			if (!getPageParameters().getNamedKeys().contains("id")) {
				final Page page = getPage();
				if (page instanceof IGetEntityId) {
					getPageParameters().add("id", ((IGetEntityId)page).getEntityId());
				} else {
					final StringValue parameter = page.getPageParameters().get("id");
					if (parameter == null || parameter.toString() == null) {
						throw new IllegalStateException("BookmarkableEntityInstanceNavigationHandler link: the Page doesn't implement IGetEntityId (page class " + page.getClass().getCanonicalName() + ") and doesn't have an id page parameter either; pageParameters: " + page.getPageParameters());
					}
					getPageParameters().add("id", parameter.toString());
				}
			}

		}

	}
}
