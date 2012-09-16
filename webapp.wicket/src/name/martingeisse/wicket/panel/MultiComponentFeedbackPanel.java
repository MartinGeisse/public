/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.lang.Objects;

/**
 * Like {@link ComponentFeedbackPanel}, except that it shows feedback
 * messages from any of a set of components.
 */
public class MultiComponentFeedbackPanel extends FeedbackPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param components the components to show feedback messages from
	 */
	public MultiComponentFeedbackPanel(final String id, final Component... components) {
		super(id, new MessageFilter(components));
	}

	/**
	 * Like {@link ComponentFeedbackMessageFilter}, except that it shows feedback
	 * messages from any of a set of components.
	 */
	public static class MessageFilter implements IFeedbackMessageFilter {

		/**
		 * the components
		 */
		private final Component[] components;

		/**
		 * Constructor.
		 * @param components the components to show feedback messages from
		 */
		public MessageFilter(final Component... components) {
			this.components = components;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.feedback.IFeedbackMessageFilter#accept(org.apache.wicket.feedback.FeedbackMessage)
		 */
		@Override
		public boolean accept(final FeedbackMessage message) {
			Component reporter = message.getReporter();
			for (Component component : components) {
				if (Objects.equal(component, reporter)) {
					return true;
				}
			}
			return false;
		}

	}

}
