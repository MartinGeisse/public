/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import name.martingeisse.guiserver.backend.BackendHttpClient;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * A wicket model that loads text using HTTP.
 */
public class HttpModel extends LoadableDetachableModel<String> {

	/**
	 * the url
	 */
	private final String url;

	/**
	 * Constructor.
	 * @param url the URL to load from
	 */
	public HttpModel(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected String load() {
		try {
			return BackendHttpClient.getText(url);
		} catch (Exception e) {
			return "ERROR";			
		}
	}

}
