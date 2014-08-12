/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.Component;

/**
 * This feedback panel better adapts to Bootstrap CSS classes.
 */
public class BootstrapComponentFeedbackPanel extends org.apache.wicket.markup.html.panel.ComponentFeedbackPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param filter the filter
	 */
	public BootstrapComponentFeedbackPanel(String id, Component filter) {
		super(id, filter);
	}
	
}
