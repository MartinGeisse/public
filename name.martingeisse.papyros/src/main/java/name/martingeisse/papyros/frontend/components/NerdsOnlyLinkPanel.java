/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend.components;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Shows a warning box saying "this is for nerds only" to prevent
 * other users from breaking things. The box contains a link to
 * some "technical" part of the system.
 */
public class NerdsOnlyLinkPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the Wicket id
	 * @param pageClass the page class to link to
	 * @param pageParameters the parameters to include in the link
	 */
	public NerdsOnlyLinkPanel(String id, Class<? extends WebPage> pageClass, PageParameters pageParameters) {
		super(id);
		internalInit(pageClass, pageParameters);
	}

	/**
	 * Constructor.
	 * @param id the Wicket id
	 * @param pageClass the page class to link to
	 * @param pageParameters the parameters to include in the link
	 * @param model the model
	 */
	public NerdsOnlyLinkPanel(String id, Class<? extends WebPage> pageClass, PageParameters pageParameters, IModel<?> model) {
		super(id, model);
		internalInit(pageClass, pageParameters);
	}
	
	/**
	 * 
	 */
	private void internalInit(Class<? extends WebPage> pageClass, PageParameters pageParameters) {
		add(new BookmarkablePageLink<Void>("link", pageClass, pageParameters));
	}
	
}
