/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * This feedback panel better adapts to Bootstrap CSS classes.
 */
public class BootstrapFeedbackPanel extends FeedbackPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param filter the filter
	 */
	public BootstrapFeedbackPanel(String id, IFeedbackMessageFilter filter) {
		super(id, filter);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public BootstrapFeedbackPanel(String id) {
		super(id);
	}

}
