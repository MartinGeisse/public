/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.HttpModel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

/**
 * This configuration represents a wicket panel that loads its content from the backend.
 */
public final class IncludeBackendConfiguration extends AbstractComponentConfiguration {

	/**
	 * the url
	 */
	private final String url;

	/**
	 * the escape
	 */
	private final boolean escape;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param url the URL to load from
	 * @param escape whether to escape HTML special characters
	 */
	public IncludeBackendConfiguration(String id, String url, boolean escape) {
		super(id);
		this.url = url;
		this.escape = escape;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new Label(getId(), new HttpModel(url)).setEscapeModelStrings(escape);
	}

}
