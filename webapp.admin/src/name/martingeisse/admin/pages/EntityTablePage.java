/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.multi.IGlobalEntityListPresenter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.StringResourceModel;
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
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
	
		// create components
		String entityNameKey = ("schema.entity." + EntityConfigurationUtil.getGeneralEntityConfiguration().getEntityName(entity));
		StringResourceModel entityDisplayNameModel = new StringResourceModel(entityNameKey, this, null);
		getMainContainer().add(new Label("entityName", entityDisplayNameModel));
		getMainContainer().add(new ListView<IGlobalEntityListPresenter>("presenters", entity.getGlobalListPresenters()) {
			@Override
			protected void populateItem(ListItem<IGlobalEntityListPresenter> item) {
				final PageParameters parameters = new PageParameters();
				parameters.add("entity", EntityTablePage.this.entity.getTableName());
				parameters.add("presenter", item.getModelObject().getUrlId());
				final BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", EntityTablePage.class, parameters);
				link.add(new Label("title", item.getModelObject().getTitle(EntityTablePage.this.entity)));
				item.add(link);
			}
		});
		getMainContainer().add(new Label("presenterTitle", presenter.getTitle(entity)));
		Panel panel = presenter.createPanel("tablePresentation", entity, getPageParameters());
		getMainContainer().add(panel);
		IPageable pageable = presenter.getPageableForPanel(panel);
		if (pageable == null) {
			getMainContainer().add(new WebComponent("topPagingNavigator"));
			getMainContainer().add(new WebComponent("bottomPagingNavigator"));
		} else {
			getMainContainer().add(new PagingNavigator("topPagingNavigator", pageable));
			getMainContainer().add(new PagingNavigator("bottomPagingNavigator", pageable));
		}
		
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}
	
}
