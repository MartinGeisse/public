/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

/**
 * Filters feedback messages based on the field name from a {@link FieldPathBehavior}.
 */
public final class FieldPathFeedbackMessageFilter implements IFeedbackMessageFilter {

	/**
	 * the path
	 */
	private final String path;

	/**
	 * Constructor.
	 * @param path the field path to show feedback messages for
	 */
	public FieldPathFeedbackMessageFilter(String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.feedback.IFeedbackMessageFilter#accept(org.apache.wicket.feedback.FeedbackMessage)
	 */
	@Override
	public boolean accept(FeedbackMessage message) {
		Component component = message.getReporter();
		if (component != null) {
			for (FieldPathBehavior fieldPathBehavior : component.getBehaviors(FieldPathBehavior.class)) {
				if (fieldPathBehavior.getPath().equals(path)) {
					return true;
				}
			}
		}
		return false;
	}

}
