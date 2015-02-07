/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import name.martingeisse.guiserver.gui.FieldPathBehavior;
import name.martingeisse.guiserver.gui.FieldPathFeedbackMessageFilter;

/**
 * A panel that shows feedback messages for a form component with a {@link FieldPathBehavior}.
 */
public final class FieldPathFeedbackPanelConfiguration extends AbstractComponentConfiguration {

	/**
	 * the path
	 */
	private final String path;
	
	/**
	 * Constructor.
	 * @param id the wicket id of the feedback panel
	 * @param path the field path to show feedback messages for
	 */
	public FieldPathFeedbackPanelConfiguration(String id, String path) {
		super(id);
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new FeedbackPanel(getId(), new FieldPathFeedbackMessageFilter(path));
	}

}
