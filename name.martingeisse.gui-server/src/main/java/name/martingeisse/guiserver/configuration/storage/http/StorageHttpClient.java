/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.storage.http;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import name.martingeisse.guiserver.configuration.storage.StorageException;
import name.martingeisse.httpclient.NullCookieStore;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * This class keeps a singleton {@link HttpClient} to load the configuration.
 */
final class StorageHttpClient {

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
	 * Getter method for the httpclient.
	 * @return the httpclient
	 */
	public static HttpClient getHttpclient() {
		return httpClient;
	}

	/**
	 * Returns the charset from the content type of the specified response.
	 * 
	 * @param response the response
	 * @return the charset, using ISO-8859-1 as the fallback if not specified or not known
	 */
	private static Charset getContentTypeCharset(HttpResponse response) {
		Header contentType = response.getEntity().getContentType();
		if (contentType.getElements().length > 0) {
			HeaderElement element = contentType.getElements()[0];
			NameValuePair charsetNvp = element.getParameterByName("charset");
			if (charsetNvp != null) {
				String charsetName = charsetNvp.getValue();
				try {
					return Charset.forName(charsetName);
				} catch (Exception e) {
				}
			}
		}
		return StandardCharsets.UTF_8;
	}

	/**
	 * Fetches a response from the backend using a GET request.
	 * Throws an exception on errors.
	 * 
	 * @param url the URL to fetch from
	 * @return the response
	 */
	public static HttpResponse get(String url) throws StorageException {
		final HttpGet httpGet = new HttpGet(url);
		final HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			throw new StorageException("could not connect to configuration storage", e);
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new StorageException("got wrong status code for " + url + " -- " + response.getStatusLine());
		}
		return response;
	}

	/**
	 * Fetches text content from the backend using a GET request.
	 * Throws an exception on errors.
	 * 
	 * @param url the URL to fetch from
	 * @return the text
	 */
	public static String getText(String url) throws StorageException {
		final HttpResponse response = get(url);
		final Charset charset = getContentTypeCharset(response);
		try (Reader reader = new InputStreamReader(response.getEntity().getContent(), charset)) {
			return IOUtils.toString(reader);
		} catch (IOException e) {
			throw new StorageException("I/O errors while reading configuration", e);
		}
	}

	/**
	 * Prevent instantiation.
	 */
	private StorageHttpClient() {
	}

}
