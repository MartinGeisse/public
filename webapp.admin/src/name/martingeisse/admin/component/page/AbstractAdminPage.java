/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import name.martingeisse.admin.component.pagebar.PageBarUtil;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * The base class for all admin pages.
 */
public class AbstractAdminPage extends WebPage {

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
	 * Returns the main container that contains the actual page components.
	 * @return the main container
	 */
	public MarkupContainer getMainContainer() {
		return this;
	}
	
	/**
	 * Returns a sub-component of the main container. This method must be used instead
	 * of getMainContainer().get(id) because the get(id) method of Wicket borders
	 * doesn't return components from the border's body -- it returns components
	 * from the border itself. (The main container currently isn't a border, but
	 * this method abstracts from that).
	 * 
	 * @param id the wicket id of the component to return
	 * @return the component
	 */
	public Component getFromMainContainer(String id) {
		return get(id);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(PageBarUtil.createAllPageTopBars(this, "pageTopBars"));
		add(PageBarUtil.createAllPageBottomBars(this, "pageBottomBars"));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference(new CssResourceReference(AbstractAdminPage.class, "common.css"));
		response.renderJavaScriptReference(new JavaScriptResourceReference(AbstractAdminPage.class, "common.js"));
		response.renderCSSReference(new CssResourceReference(AbstractAdminPage.class, "jquery.dataTables.css"));
	}

}
