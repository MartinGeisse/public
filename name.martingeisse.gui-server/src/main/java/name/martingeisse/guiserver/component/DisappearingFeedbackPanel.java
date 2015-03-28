/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * A {@link FeedbackPanel} that becomes invisible whenever it
 * doesn't have any messages.
 */
public class DisappearingFeedbackPanel extends FeedbackPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param filter the message filter
	 */
	public DisappearingFeedbackPanel(String id, IFeedbackMessageFilter filter) {
		super(id, filter);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public DisappearingFeedbackPanel(String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onConfigure()
	 */
	@Override
	protected void onConfigure() {
		setVisible(anyMessage());
	}

}
