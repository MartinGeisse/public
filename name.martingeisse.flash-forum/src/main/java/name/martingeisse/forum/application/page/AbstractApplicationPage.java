/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.application.page;

import org.apache.wicket.ajax.WicketAjaxJQueryResourceReference;
import org.apache.wicket.ajax.WicketEventJQueryResourceReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;

/**
 * Base class for all application pages. This class includes
 * the common stylesheets and Javascripts.
 */
public class AbstractApplicationPage extends WebPage {

	/**
	 * Constructor.
	 */
	public AbstractApplicationPage() {
		super();
	}

	/**
	 * Constructor.
	 * @param model the page model
	 */
	public AbstractApplicationPage(final IModel<?> model) {
		super(model);
	}

	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public AbstractApplicationPage(final PageParameters parameters) {
		super(parameters);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(new PriorityHeaderItem(CssHeaderItem.forReference(new CssResourceReference(AbstractApplicationPage.class, "common.css"))));
		response.render(new PriorityHeaderItem(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractApplicationPage.class, "common.js"))));
		markJavascriptAlreadyRendered(response, JQueryResourceReference.get());
		markJavascriptAlreadyRendered(response, WicketAjaxJQueryResourceReference.get());
		markJavascriptAlreadyRendered(response, WicketEventJQueryResourceReference.get());
	}

	/**
	 * 
	 */
	private void markJavascriptAlreadyRendered(IHeaderResponse response, ResourceReference reference) {
		JavaScriptHeaderItem jQueryHeaderItem = JavaScriptHeaderItem.forReference(reference);
		for (Object renderToken : jQueryHeaderItem.getRenderTokens()) {
			response.markRendered(renderToken);
		}
	}
	
}
