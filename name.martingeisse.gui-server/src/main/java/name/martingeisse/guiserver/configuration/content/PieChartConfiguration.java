/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.PieChartImageResource;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.image.Image;

/**
 * Configuration for a pie chart component.
 */
public final class PieChartConfiguration extends AbstractComponentConfiguration {

	/**
	 * the backendUrl
	 */
	private final String backendUrl;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param backendUrl the URL to load chart data from
	 */
	public PieChartConfiguration(String id, String backendUrl) {
		super(id);
		this.backendUrl = backendUrl;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new Image(getId(), new PieChartImageResource(500, 300, backendUrl));
	}
	
}
