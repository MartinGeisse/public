/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.mount;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * TODO: document me
 *
 */
public class MountTestPage extends WebPage {

	/**
	 * Constructor.
	 * @param parameters ...
	 */
	public MountTestPage(final PageParameters parameters) {
		super(parameters);
		add(new Label("foo", parameters.get("foo").toString()));
		add(new Label("bar", parameters.get("bar").toString()));
		add(new Link<Void>("link") {

			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				System.out.println("***");
			}
			
		});
	}

}
