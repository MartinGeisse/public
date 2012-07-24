/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base class for paginated pages, i.e. pages with navigation
 * for paginated lists. Subclasses must implement obtaining the
 * {@link IPageable} to enable paging support. This page allows
 * to disable paging support by returning null in that method.
 */
public abstract class AbstractPaginatedAdminPage extends AbstractAdminPage {

	/**
	 * Constructor.
	 */
	public AbstractPaginatedAdminPage() {
		super();
	}

	/**
	 * Constructor.
	 * @param model the page model
	 */
	public AbstractPaginatedAdminPage(final IModel<?> model) {
		super(model);
	}

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractPaginatedAdminPage(final PageParameters parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		getMainContainer().add(new Label("entityName", createTitleModel()));
	}

	/**
	 * This method must be called by the onInitialize() method of the subclass. The paging
	 * navigators are not created by onInitialize() to avoid problems when getPageable()
	 * isn't yet guaranteed to return a valid result.
	 */
	protected final void initializePagingNavigators(final IPageable pageable) {
		if (pageable == null) {
			getMainContainer().add(new WebComponent("topPagingNavigator"));
			getMainContainer().add(new WebComponent("bottomPagingNavigator"));
		} else {
			getMainContainer().add(new PagingNavigator("topPagingNavigator", pageable));
			getMainContainer().add(new PagingNavigator("bottomPagingNavigator", pageable));
		}
	}

	/**
	 * Creates a model for the page title.
	 * @return the title model
	 */
	protected abstract IModel<String> createTitleModel();

	/**
	 * Returns the {@link IPageable} for automatic paging support, or null to disable
	 * automatic paging support.
	 * @return the pageable or null
	 */
	protected abstract IPageable getPageable();

}
