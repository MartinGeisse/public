/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.backend;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.httpclient.NullCookieStore;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

/**
 * This class keeps a singleton {@link HttpClient}. It also provides convenience
 * methods to send HTTP requests to the backend.
 */
public final class BackendHttpClient {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(BackendHttpClient.class);
	
	/**
	 * the connectionManager
	 */
	private static final PoolingHttpClientConnectionManager connectionManager;

	/**
	 * the httpClient
	 */
	private static final HttpClient httpClient;
	
	/**
	 * the urlCodec
	 */
	private static final URLCodec urlCodec = new URLCodec();
	
	/**
	 * the commandResponseParser
	 */
	private static final IBackendCommandResponseParser commandResponseParser;

	//
	static {
		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
		connectionManager.setDefaultMaxPerRoute(20);
		httpClient = HttpClients.custom().setConnectionManager(connectionManager).setDefaultCookieStore(new NullCookieStore()).build();
		commandResponseParser = new DefaultBackendCommandResponseParser();
	}

	/**
	 * Getter method for the httpclient.
	 * @return the httpclient
	 */
	public static HttpClient getHttpclient() {
		return httpClient;
	}

	/**
	 * Returns the base content type of the specified response.
	 * 
	 * @param response the response
	 * @return the base content type, or null if not specified
	 */
	private static String getBaseContentType(HttpResponse response) {
		Header contentType = response.getEntity().getContentType();
		if (contentType == null) {
			return null;
		}
		HeaderElement[] elements = contentType.getElements();
		return (elements.length == 0 ? null : elements[0].getName());
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
	 * Expects a JSON response and returns a {@link JsonAnalyzer} for it.
	 * 
	 * @return the analyzer
	 */
	private static JsonAnalyzer expectJsonResponse(HttpResponse response) {
		String baseContentType = getBaseContentType(response);
		if (!("application/json".equals(baseContentType) || "text/x-json".equals(baseContentType))) {
			logger.error("backend didn't return JSON");
			throw new BackendConnectionException();
		}
		// JSON always uses UTF-8
		try (Reader reader = new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8)) {
			return JsonAnalyzer.parse(reader);
		} catch (IOException e) {
			logger.error("couldn't handle backend response", e);
			throw new BackendConnectionException();
		}
	}

	/**
	 * Expects a (JSON-encoded) backend command response and returns it.
	 * 
	 * @return the response
	 */
	private static BackendCommandResponse expectCommandResponse(HttpResponse response) {
		return commandResponseParser.parseBackendCommandResponse(expectJsonResponse(response));
	}

	/**
	 * Fetches a response from the backend using a GET request.
	 * Throws an exception on errors.
	 * 
	 * @param url the URL to fetch from
	 * @return the response
	 */
	public static HttpResponse get(String url) {
		final HttpGet httpGet = new HttpGet(url);
		final HttpResponse response;
		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
			logger.error("couldn't connect to the backend", e);
			throw new BackendConnectionException();
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			logger.error("got wrong status code for " + url + " -- " + response.getStatusLine());
			throw new BackendConnectionException();
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
	public static String getText(String url) {
		final HttpResponse response = get(url);
		final Charset charset = getContentTypeCharset(response);
		try (Reader reader = new InputStreamReader(response.getEntity().getContent(), charset)) {
			return IOUtils.toString(reader);
		} catch (IOException e) {
			logger.error("couldn't handle backend response", e);
			throw new BackendConnectionException();
		}
	}
	
	/**
	 * Fetches JSON content from the backend using a GET request.
	 * Throws an exception on errors.
	 * 
	 * @param url the URL to fetch from
	 * @return the JSON
	 */
	public static JsonAnalyzer getJson(String url) {
		return expectJsonResponse(get(url));
	}

	/**
	 * Fetches a command response from the backend using a GET request.
	 * Throws an exception on errors.
	 * 
	 * @param url the URL to fetch from
	 * @return the command response
	 */
	public static BackendCommandResponse getCommandResponse(String url) {
		return expectCommandResponse(get(url));
	}

	/**
	 * Sends a POST request with the specified parameters to the backend.
	 * 
	 * @param url the URL to POST to
	 * @param parameters the parameters
	 * @return the response
	 */
	public static HttpResponse postParametersForResponse(String url, Map<String, Object> parameters) {
		final HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpPost.setEntity(new StringEntity(encodeFormParameters(parameters), StandardCharsets.US_ASCII));
		final HttpResponse response;
		try {
			response = httpClient.execute(httpPost);
		} catch (Exception e) {
			logger.error("couldn't connect to the backend", e);
			throw new BackendConnectionException();
		}
		if (response.getStatusLine().getStatusCode() != 200) {
			logger.error("got wrong status code for " + url + " -- " + response.getStatusLine());
			throw new BackendConnectionException();
		}
		return response;
	}
	
	/**
	 * Encodes a parameter object using form encoding.
	 * 
	 * @param parameters the parameters.
	 * @return the encoded parameters
	 */
	private static String encodeFormParameters(Map<String, Object> parameters) {
		try {
			StringBuilder builder = new StringBuilder();
			for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
				String name = parameter.getKey();
				String value = Objects.toString(parameter.getValue(), "");
				if (builder.length() > 0) {
					builder.append('&');
				}
				builder.append(name).append('=').append(urlCodec.encode(value));
			}
			return builder.toString();
		} catch (EncoderException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Sends a POST request with the specified parameters to the backend and expects
	 * a JSON response.
	 * 
	 * @param url the URL to POST to
	 * @param parameters the parameters
	 * @return the response
	 */
	public static JsonAnalyzer postParametersForJson(String url, Map<String, Object> parameters) {
		return expectJsonResponse(postParametersForResponse(url, parameters));
	}
	
	/**
	 * Sends a POST request with the specified parameters to the backend and expects
	 * a command response.
	 * 
	 * @param url the URL to POST to
	 * @param parameters the parameters
	 * @return the command response
	 */
	public static BackendCommandResponse postParametersForCommandResponse(String url, Map<String, Object> parameters) {
		return expectCommandResponse(postParametersForResponse(url, parameters));
	}
	
	/**
	 * Prevent instantiation.
	 */
	private BackendHttpClient() {
	}

}
