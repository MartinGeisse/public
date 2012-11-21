/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.page;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

/**
 * A link that once clicked opens a page that contains the model
 * value of the link converted to a string.
 * 
 * NOTE: The created page is stateful since it must store the model.
 * @param <T> the model type
 */
public class TextPageLink<T> extends Link<T> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public TextPageLink(final String id, final IModel<T> model) {
		super(id, model);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public TextPageLink(final String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.link.Link#onClick()
	 */
	@Override
	public void onClick() {
		TextPage page = new TextPage(getModel());
		page.setEscapeModelStrings(getEscapeModelStrings());
		setResponsePage(page);
	}
	
}
