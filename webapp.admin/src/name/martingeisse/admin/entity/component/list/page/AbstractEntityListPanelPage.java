/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.page;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.list.IEntityListFilterAcceptor;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.util.IGetPageable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class allows to display an entity list using an existing panel
 * class that takes a model of type {@link EntityInstance}. The concrete
 * subclass must supply the panel class and strategies for the panel
 * (such as an entity list filter).
 * 
 * The panel may implement either {@link IPageable} or {@link IGetPageable}
 * to enable framework paging support.
 * 
 * The panel may implement {@link IEntityListFilterAcceptor} to receive
 * entity list filters from the navigation node.
 */
public abstract class AbstractEntityListPanelPage extends AbstractEntityListPage {

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractEntityListPanelPage(final PageParameters parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		// create the panel
		final Class<? extends Panel> panelClass = determinePanelClass();
		final IModel<EntityDescriptor> model = determineEntityTypeModel();
		final Panel panel;
		try {
			panel = panelClass.getConstructor(String.class, IModel.class).newInstance("panel", model);
		} catch (final NoSuchMethodException e) {
			throw new RuntimeException("entity list panel " + panelClass.getCanonicalName() + " has no constructor(String, IModel).");
		} catch (final Exception e) {
			throw new RuntimeException("exception while invoking entity list panel constructor of panel class " + panelClass.getCanonicalName(), e);
		}
		
		// if the panel accepts entity list filters, fetch them from the navigation node
		if (panel instanceof IEntityListFilterAcceptor) {
			IEntityListFilterAcceptor filterAcceptor = (IEntityListFilterAcceptor)panel;
			IEntityListFilter filter = determineEntityListFilter();
			if (filter != null) {
				filterAcceptor.acceptEntityListFilter(filter);
			}
		}
		
		// Add the panel as the last step. Since we're in onInitialize(), this causes the onInitialize()
		// of the panel to be called immediately, and that must happen *after* the filter is set!
		getMainContainer().add(panel);

		// initialize the paging navigators last because they need a pageable, which getPageable() returns only
		// when the panel is in place.
		initializePagingNavigators(getPageable());
		
	}

	/**
	 * Determines the panel class to use.
	 * @return the panel class
	 */
	protected abstract Class<? extends Panel> determinePanelClass();

	/**
	 * Determines the entity list filter to use.
	 * @return the entity list filter
	 */
	protected abstract IEntityListFilter determineEntityListFilter();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityListPage#getPageable()
	 */
	@Override
	protected IPageable getPageable() {
		Component panel = getFromMainContainer("panel");
		if (panel instanceof IGetPageable) {
			IGetPageable getPageable = (IGetPageable)panel;
			return getPageable.getPageable();
		} else if (panel instanceof IPageable) {
			return (IPageable)panel;
		} else {
			return null;
		}
	}

}
