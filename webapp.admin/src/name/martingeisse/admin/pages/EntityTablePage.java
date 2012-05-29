/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.schema.EntityDescriptor;

import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This page displays a global entity table.
 */
public class EntityTablePage extends AbstractAdminPage {

	/**
	 * the entity
	 */
	private EntityDescriptor entity;
	
	/**
	 * the presenter
	 */
	private IGlobalEntityListPresenter presenter;
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public EntityTablePage(final PageParameters parameters) {
		super(parameters);
		entity = determineEntity(parameters);
		
		// determine the presenter
		String presenterName = parameters.get("presenter").toString("default");
		presenterName = (presenterName.isEmpty() ? "default" : presenterName);
		presenter = entity.getGlobalListPresenter(presenterName);
		if (presenter == null) {
			throw new RuntimeException("unknown presenter: " + presenterName);
		}
		
		// create components
		add(new Label("title", presenter.getTitle(entity)));
		Panel panel = presenter.createPanel("tablePresentation", entity, parameters);
		add(panel);
		IPageable pageable = presenter.getPageableForPanel(panel);
		if (pageable == null) {
			add(new WebComponent("tablePresentation"));
		} else {
			add(new PagingNavigator("pagingNavigator", pageable));
		}
		
	}

}
