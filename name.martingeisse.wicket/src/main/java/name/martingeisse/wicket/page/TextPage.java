/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.page;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * A simple page that displays its model value as a string.
 */
public class TextPage extends WebPage {

	/**
	 * Constructor.
	 * @param model the model
	 */
	public TextPage(IModel<?> model) {
		add(new Label("content", model));
	}
	
}
