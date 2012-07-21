/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilterConsumer;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.navigation.handler.EntityInstancePanelHandler;
import name.martingeisse.admin.util.IGetPageable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class allows to display an entity list using an existing panel
 * class that takes a model of type {@link EntityInstance}. This class
 * is primarily used in combination with {@link EntityInstancePanelHandler}
 * to mount entity instance panels in the navigation.
 * 
 * The panel may implement either {@link IPageable} or {@link IGetPageable}
 * to enable framework paging support.
 * 
 * The panel may implement {@link IEntityListFilterConsumer} to receive
 * entity list filters from the navigation node.
 */
public class EntityListPanelPage extends AbstractEntityListPage {

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public EntityListPanelPage(final PageParameters parameters) {
		super(parameters);
	}

	/**
	 * @return the panel class
	 */
	private Class<? extends Panel> determinePanelClass() {
		final String className = getRequiredStringParameter(getPageParameters(), "panelClass", true).toString();
		try {
			return Class.forName(className).asSubclass(Panel.class);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("no such panel class: " + className);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		// create the panel
		final Class<? extends Panel> panelClass = determinePanelClass();
		final IModel<EntityDescriptor> model = new EntityDescriptorModel();
		final Panel panel;
		try {
			panel = panelClass.getConstructor(String.class, IModel.class).newInstance("panel", model);
		} catch (final NoSuchMethodException e) {
			throw new RuntimeException("entity list panel " + panelClass.getCanonicalName() + " has no constructor(String, IModel).");
		} catch (final Exception e) {
			throw new RuntimeException("exception while invoking entity list panel constructor of panel class " + panelClass.getCanonicalName(), e);
		}
		
		// if the panel accepts entity list filters, fetch them from the navigation node
		if (panel instanceof IEntityListFilterConsumer) {
			IEntityListFilterConsumer consumer = (IEntityListFilterConsumer)panel;
			IEntityListFilter filter = getEntityListFilter();
			if (filter != null) {
				consumer.setEntityListFilter(filter);
			}
		}
		
		// Add the panel as the last step. Since we're in onInitialize(), this causes the onInitialize()
		// of the panel to be called immediately, and that must happen *after* the filter is set!
		getMainContainer().add(panel);
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityListPage#getPageable()
	 */
	@Override
	protected IPageable getPageable() {
		Component panel = getMainContainer().get("panel");
		if (panel instanceof IGetPageable) {
			IGetPageable getPageable = (IGetPageable)panel;
			return getPageable.getPageable();
		} else if (panel instanceof IPageable) {
			return (IPageable)panel;
		} else {
			return null;
		}
	}

	/**
	 * This model returns the entity descriptor.
	 */
	private class EntityDescriptorModel extends LoadableDetachableModel<EntityDescriptor> {

		/* (non-Javadoc)
		 * @see org.apache.wicket.model.LoadableDetachableModel#load()
		 */
		@Override
		protected EntityDescriptor load() {
			return determineEntity(getPageParameters());
		}

	}
}
