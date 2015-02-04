/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import name.martingeisse.httpclient.NullCookieStore;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * A wicket model that loads text using HTTP.
 */
public class HttpModel extends LoadableDetachableModel<String> {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(HttpModel.class);
	
	/**
	 * the connectionManager
	 */
	private static final PoolingHttpClientConnectionManager connectionManager;
	
	/**
	 * the httpClient
	 */
	private static final HttpClient httpClient;
	
	//
	static {
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultCookieStore(new NullCookieStore()).build();
	}
	
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
			final HttpGet httpGet = new HttpGet(url);
			final HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() != 200) {
				logger.error("got wrong status code for " + url + " -- " + response.getStatusLine());
				return "ERROR";
			}
			Header contentEncodingHeader = response.getEntity().getContentEncoding();
			String encodingName = (contentEncodingHeader == null ? "UTF-8" : contentEncodingHeader.getValue());
			try (Reader reader = new InputStreamReader(response.getEntity().getContent(), Charset.forName(encodingName))) {
				return IOUtils.toString(reader);
			}
		} catch (final Exception e) {
			logger.error("exception while fetching data for an HttpModel", e);
			return "ERROR";
		}
	}

}
