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
public final class PieChartConfiguration extends AbstractComponentConfiguration implements IConfigurationSnippet {

	/**
	 * the backendUrl
	 */
	private final String backendUrl;
	
	/**
	 * the legend
	 */
	private final boolean legend;

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param backendUrl the URL to load chart data from
	 * @param legend whether to draw a legend
	 */
	public PieChartConfiguration(String id, String backendUrl, boolean legend) {
		super(id);
		this.backendUrl = backendUrl;
		this.legend = legend;
	}
	
	/**
	 * Getter method for the backendUrl.
	 * @return the backendUrl
	 */
	public String getBackendUrl() {
		return backendUrl;
	}
	
	/**
	 * Getter method for the legend.
	 * @return the legend
	 */
	public boolean isLegend() {
		return legend;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new Image(getId(), new PieChartImageResource(500, 300, this));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.IConfigurationSnippet#setSnippetHandle(int)
	 */
	@Override
	public void setSnippetHandle(int handle) {
		this.snippetHandle = handle;
	}
	
	/**
	 * Getter method for the snippetHandle.
	 * @return the snippetHandle
	 */
	public int getSnippetHandle() {
		return snippetHandle;
	}

}
