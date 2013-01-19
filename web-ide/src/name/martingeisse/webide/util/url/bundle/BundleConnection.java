/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.util.url.bundle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QPluginBundles;

import com.mysema.query.sql.SQLQuery;

/**
 * Custom {@link URLConnection} to load the JAR files for plugin bundles.
 */
public class BundleConnection extends URLConnection {

	/**
	 * the pluginBundleId
	 */
	private long pluginBundleId;

	/**
	 * the data
	 */
	private byte[] data;

	/**
	 * the inputStream
	 */
	private ByteArrayInputStream inputStream;

	/**
	 * Constructor.
	 * @param url the url
	 * @throws MalformedURLException if the URL is invalid
	 */
	public BundleConnection(final URL url) throws MalformedURLException {
		super(url);

		// some checks
		if (!url.getProtocol().equals("bundle")) {
			throw new MalformedURLException("wrong protocol: " + url.getProtocol());
		}

		// parse the URL
		try {
			pluginBundleId = Long.parseLong(url.getFile());
		} catch (final NumberFormatException e) {
			throw new MalformedURLException("the 'file' part of the URL must be the plugin bundle id but is: " + url.getFile());
		}

	}

	/* (non-Javadoc)
	 * @see java.net.URLConnection#connect()
	 */
	@Override
	public void connect() throws IOException {
		if (data == null) {
			final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
			query.from(QPluginBundles.pluginBundles);
			query.where(QPluginBundles.pluginBundles.id.eq(pluginBundleId));
			data = query.singleResult(QPluginBundles.pluginBundles).getJarfile();
			inputStream = new ByteArrayInputStream(data);
		}
	}

	/* (non-Javadoc)
	 * @see java.net.URLConnection#getContentLength()
	 */
	@Override
	public int getContentLength() {
		return (data == null ? -1 : data.length);
	}

	/* (non-Javadoc)
	 * @see java.net.URLConnection#getContentType()
	 */
	@Override
	public String getContentType() {
		return "application/java-archive";
	}

	/* (non-Javadoc)
	 * @see java.net.URLConnection#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		connect();
		return inputStream;
	}

}
