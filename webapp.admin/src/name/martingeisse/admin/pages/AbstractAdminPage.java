/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.common.Dummy;
import name.martingeisse.admin.schema.ApplicationSchema;
import name.martingeisse.admin.schema.EntityDescriptor;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.string.StringValue;

/**
 * The base class for all admin pages.
 */
public class AbstractAdminPage extends WebPage {

	/**
	 * Constructor.
	 */
	public AbstractAdminPage() {
		super();
		addPageBorder();
	}

	/**
	 * Constructor.
	 * @param model the page model
	 */
	public AbstractAdminPage(IModel<?> model) {
		super(model);
		addPageBorder();
	}

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractAdminPage(PageParameters parameters) {
		super(parameters);
		addPageBorder();
	}

	/**
	 * 
	 */
	private void addPageBorder() {
		add(ApplicationConfiguration.createPageBorder("pageBorder"));
	}
	
	/**
	 * Returns the main container that contains the page contents. Subclasses must
	 * add child components to this container.
	 * @return the main container
	 */
	public WebMarkupContainer getMainContainer() {
		return (WebMarkupContainer)get("pageBorder");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		getMainContainer().add(new ListView<EntityDescriptor>("entities", ApplicationSchema.instance.getEntityDescriptors()) {
			@Override
			protected void populateItem(ListItem<EntityDescriptor> item) {
				PageParameters parameters = new PageParameters();
				parameters.add("entity", item.getModelObject().getTableName());
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", EntityTablePage.class, parameters);
				link.add(new Label("name", ApplicationConfiguration.getEntityDisplayName(item.getModelObject())));
				item.add(link);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference(new CssResourceReference(Dummy.class, "common.css"));
	}
	
	/**
	 * Tries to return the parameter with the specified name. TODO should
	 * go to an error page if this is not possible.
	 * @param parameters the page parameters
	 * @param name the name of the parameter to return
	 * @param mountedOnly whether this parameter only appears as a mounted parameter. If so,
	 * then the parameter not being passed indicates a bug in the application, not just an
	 * invalid URL, since the page that requires the parameter should not have been used in
	 * the first place if the parameter is missing. This triggers slightly different error
	 * logging.
	 * @return the parameter value
	 */
	protected StringValue getRequiredStringParameter(PageParameters parameters, String name, boolean mountedOnly) {
		if (parameters.getNamedKeys().contains(name)) {
			return parameters.get(name);
		}
		if (mountedOnly) {
			throw new RuntimeException("missing mounted-only parameter '" + name + "' in page class: " + getClass());
		} else {
			// TODO: show a proper error page instead of throwing an exception
			throw new RuntimeException("missing parameter: " + name);
		}
	}

	/**
	 * Looks up the 'entity' parameter and returns the {@link EntityDescriptor} for it. This method
	 * throws an exception (TODO error page) if the parameter is missing or is not the name
	 * of any entity.
	 * @param parameters the page parameters
	 * @return the entity descriptor
	 */
	protected EntityDescriptor determineEntity(PageParameters parameters) {
		String entityName = getRequiredStringParameter(parameters, "entity", true).toString();
		EntityDescriptor entity = ApplicationSchema.instance.findEntity(entityName);
		if (entity == null) {
			throw new RuntimeException("TODO: error page; entity not found: " + entityName);
		}
		return entity;
	}
	
}
