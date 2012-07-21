/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.component.page.AbstractAdminPage;
import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.action.IEntityInstanceAction;
import name.martingeisse.admin.entity.action.IEntityInstanceActionContributor;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.single.EntityInstance;
import name.martingeisse.admin.entity.single.ISingleEntityPresenter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This class shows the presentation of an entity instance using
 * a presenter that is selected by the URL.
 */
public class EntityPresentationPage extends AbstractAdminPage {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;

	/**
	 * the id
	 */
	private final int id;
	
	/**
	 * the instance
	 */
	private transient EntityInstance instance;
	
	/**
	 * the actions
	 */
	private transient List<IEntityInstanceAction> actions;

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public EntityPresentationPage(final PageParameters parameters) {
		super(parameters);
		entity = determineEntity(parameters);
		id = getRequiredStringParameter(parameters, "id", true).toInt(); // TODO error handling
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.pages.AbstractAdminPage#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		PageParameters parameters = getPageParameters();
		
		// parse parameters
		String presenterName = parameters.get("presenter").toString("default");
		presenterName = (presenterName.isEmpty() ? "default" : presenterName);

		// this model will return the entity instance
		IModel<EntityInstance> instanceModel = new PropertyModel<EntityInstance>(this, "instance");
		
		// find the presenter
		final ISingleEntityPresenter presenter = entity.getSinglePresenter(presenterName);
		if (presenter == null) {
			throw new RuntimeException("unknown presenter: " + presenterName);
		}

		// create the presentation title label and panel
		getMainContainer().add(new Label("presenterTitle", presenter.getTitle()));
		getMainContainer().add(presenter.createPanel("presentationPanel", instanceModel));

		// create the presenter navigation
		getMainContainer().add(new ListView<ISingleEntityPresenter>("presenters", entity.getSinglePresenters()) {
			@Override
			protected void populateItem(final ListItem<ISingleEntityPresenter> item) {
				final PageParameters parameters = new PageParameters();
				parameters.add("entity", EntityPresentationPage.this.entity.getTableName());
				parameters.add("id", EntityPresentationPage.this.id);
				parameters.add("presenter", item.getModelObject().getUrlId());
				final BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", EntityPresentationPage.class, parameters);
				link.add(new Label("title", item.getModelObject().getTitle()));
				item.add(link);
			}
		});

		// create action links
		getMainContainer().add(new ListView<IEntityInstanceAction>("actions", new PropertyModel<List<IEntityInstanceAction>>(this, "actions")) {
			@Override
			protected void populateItem(final ListItem<IEntityInstanceAction> item) {
				final Link<Void> link = new Link<Void>("link") {
					@Override
					public void onClick() {
						item.getModelObject().execute(instance, entity);
					}
				};
				link.add(new Label("name", item.getModelObject().getName()));
				item.add(link);
			}
		});

	}
	
	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public EntityInstance getInstance() {
		return instance;
	}
	
	/**
	 * Getter method for the actions.
	 * @return the actions
	 */
	public List<IEntityInstanceAction> getActions() {
		return actions;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		
		// actually fetch the entity instance to show
		instance = entity.fetchSingleInstance(id);
		
		// determine instance actions
		actions = new ArrayList<IEntityInstanceAction>();
		collectActions();
		
		// base class behavior
		super.onBeforeRender();
		
	}

	/**
	 * @param actions
	 */
	private void collectActions() {
		for (final IEntityInstanceActionContributor contributor : EntityConfigurationUtil.getEntityInstanceActionContributors()) {
			collectActions(contributor.contributeGlobalActions(entity));
			collectActions(contributor.contributeInstanceActions(instance, entity));
		}
	}

	/**
	 * @param actions
	 * @param actionArray
	 */
	private void collectActions(final IEntityInstanceAction[] actionArray) {
		if (actionArray != null) {
			for (final IEntityInstanceAction action : actionArray) {
				actions.add(action);
			}
		}
	}
	
}
