/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import name.martingeisse.admin.component.pageborder.PageBorderUtil;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * The base class for all admin pages.
 */
public class AbstractAdminPage extends WebPage {

	/**
	 * the mainContainer
	 */
	private transient WebMarkupContainer mainContainer;
	
	/**
	 * Constructor.
	 */
	public AbstractAdminPage() {
		super();
	}

	/**
	 * Constructor.
	 * @param model the page model
	 */
	public AbstractAdminPage(IModel<?> model) {
		super(model);
	}

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractAdminPage(PageParameters parameters) {
		super(parameters);
	}
	
	/**
	 * Returns the main container that contains the page contents. Subclasses must
	 * add child components to this container.
	 * @return the main container
	 */
	public WebMarkupContainer getMainContainer() {
		return mainContainer;
	}
	
	/**
	 * Returns a sub-component of the main container. This method must be used instead
	 * of getMainContainer().get(id) because the get(id) method of Wicket borders
	 * doesn't return components from the border's body -- it returns components
	 * from the border itself.
	 * @param id the wicket id of the component to return
	 * @return the component
	 */
	public Component getFromMainContainer(String id) {
		String borderBodyId = (PageBorderUtil.PAGE_BORDER_ID + "_" + Border.BODY);
		return getMainContainer().get(borderBodyId).get(id);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		mainContainer = PageBorderUtil.createAllPageBorders(this);
		add(mainContainer);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference(new CssResourceReference(AbstractAdminPage.class, "common.css"));
	}
	
}
