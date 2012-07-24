/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.component.page;

import name.martingeisse.admin.component.pageborder.PageBorderUtil;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.string.StringValue;

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
	
	/**
	 * Tries to return the parameter with the specified name. TODO should
	 * go to an error page if this is not possible.
	 * @param parameters the page parameters
	 * @param name the name of the parameter to return
	 * @param requiredFromMount whether this parameter must be present due to the way the page
	 * was mounted, regardless of the URL sent by the client. If this flag is false, then a 
	 * missing parameter indicates an incorrect URL from the client. If it is true, then it
	 * indicates a bug in the application. This triggers slightly different error logging.
	 * @return the parameter value
	 */
	protected StringValue getRequiredStringParameter(PageParameters parameters, String name, boolean requiredFromMount) {
		if (parameters.getNamedKeys().contains(name)) {
			return parameters.get(name);
		}
		if (requiredFromMount) {
			throw new RuntimeException("missing parameter '" + name + "' in page class: " + getClass() +
				". This error indicates that the page was incorrectly mounted, since the parameter should be present regardless of the request sent by the client.");
		} else {
			throw new RestartResponseException(new ErrorPage("Missing request parameter '" + name + "'."));
		}
	}

}
