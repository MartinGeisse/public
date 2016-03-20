/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.configuration.storage.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import name.martingeisse.guiserver.configuration.storage.StorageException;

import org.apache.commons.lang3.StringUtils;

/**
 * Storage engine implementation that loads the configuration from
 * an Apache HTTPD.
 * 
 * TODO implement
 */
public class ApacheHttpdStorageEngine implements HttpStorageEngine {

	/**
	 * the hrefPattern
	 */
	private static final Pattern hrefPattern = Pattern.compile("\\ href=\\\"([^\\\"\\?]*)\\\"");

	/**
	 * the baseUrl
	 */
	private final String baseUrl;

	/**
	 * Constructor.
	 * @param baseUrl the base URL
	 */
	public ApacheHttpdStorageEngine(String baseUrl) {
		this.baseUrl = StringUtils.stripEnd(baseUrl, "/");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.http.HttpStorageEngine#open(java.lang.String)
	 */
	@Override
	public InputStream open(String path) throws StorageException {
		try {
			return StorageHttpClient.get(baseUrl + path).getEntity().getContent();
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.storage.http.HttpStorageEngine#list(java.lang.String)
	 */
	@Override
	public HttpFolderEntry[] list(String path) throws StorageException {
		path = StringUtils.stripEnd(path, "/");
		List<HttpFolderEntry> entries = new ArrayList<>();
		String listingHtml = StorageHttpClient.getText(baseUrl + path);
		Matcher matcher = hrefPattern.matcher(listingHtml);
		while (matcher.find()) {
			MatchResult matchResult = matcher.toMatchResult();
			String href = matchResult.group(1);
			if (href.startsWith("/")) {
				continue;
			}
			if (href.endsWith("/")) {
				String name = StringUtils.stripEnd(href, "/");
				entries.add(new HttpFolder(this, path + '/' + name, path + '/' + name));
			} else {
				String name = href;
				entries.add(new HttpElement(this, path + '/' + name, path + '/' + name));
			}
		}
		return entries.toArray(new HttpFolderEntry[entries.size()]);
	}

}
