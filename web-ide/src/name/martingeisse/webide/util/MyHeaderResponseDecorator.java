/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.util;

import name.martingeisse.webide.workbench.WorkbenchPage;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.resource.CoreLibrariesContributor;

/**
 * This decorator renders a reference to common.css (and if needed, ie.css)
 * at the beginning of the header response unless we're in an AJAX request
 * (in the latter case, common.css has already been included).
 */
public class MyHeaderResponseDecorator implements IHeaderResponseDecorator {

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.IHeaderResponseDecorator#decorate(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public IHeaderResponse decorate(final IHeaderResponse response) {
		if (AjaxRequestUtil.getAjaxRequestTarget() == null) {
			response.render(CssHeaderItem.forReference(new CssResourceReference(WorkbenchPage.class, "common.css"), null, "screen, projection"));
			response.render(CssHeaderItem.forReference(new CssResourceReference(WorkbenchPage.class, "ie.css"), null, "screen, projection", "IE"));
			CoreLibrariesContributor.contributeAjax(WebApplication.get(), response);
		}
		return response;
	}

}
