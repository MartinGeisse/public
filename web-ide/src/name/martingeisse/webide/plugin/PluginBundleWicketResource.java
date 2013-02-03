/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QPluginBundles;
import name.martingeisse.webide.resources.ResourcePath;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Duration;

import com.mysema.query.sql.SQLQuery;

/**
 * A Wicket resource that represents a plugin bundle JAR.
 */
public class PluginBundleWicketResource extends AbstractResource {

	/**
	 * the pluginBundleId
	 */
	private final long pluginBundleId;

	/**
	 * the localPath
	 */
	private final ResourcePath localPath;

	/**
	 * Constructor.
	 * @param pluginBundleId the plugin bundle ID
	 * @param localPath the local path within the plugin bundle
	 */
	public PluginBundleWicketResource(final long pluginBundleId, final ResourcePath localPath) {
		this.pluginBundleId = pluginBundleId;
		this.localPath = localPath;
		if (localPath != null && !localPath.isLeadingSeparator()) {
			throw new IllegalArgumentException("Cannot build a PluginBundleWicketResource from a relative path: " + localPath);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.AbstractResource#newResourceResponse(org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected ResourceResponse newResourceResponse(final Attributes attributes) {

		// fetch the JAR file
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QPluginBundles.pluginBundles);
		query.where(QPluginBundles.pluginBundles.id.eq(pluginBundleId));
		final Object[] row = (Object[])(Object)query.singleResult(QPluginBundles.pluginBundles.jarfile);
		final byte[] jarData = (byte[])row[0];
		String matchingEntryName;
		final byte[] matchingEntryData;

		// if requested, load a single file from the JAR
		if (localPath != null) {
			final String localPathText = localPath.withLeadingSeparator(false).withTrailingSeparator(false).toString();
			try {
				final ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(jarData));
				while (true) {
					final ZipEntry entry = zipInputStream.getNextEntry();
					if (entry == null) {
						zipInputStream.close();
						final ResourceResponse response = new ResourceResponse();
						response.setError(404, "Plugin bundle (id " + pluginBundleId + ") does not contain file: " + localPathText);
						response.setStatusCode(404);
						return response;
					}
					if (localPathText.equals(entry.getName())) {
						matchingEntryName = entry.getName();
						matchingEntryData = IOUtils.toByteArray(zipInputStream);
						zipInputStream.close();
						break;
					}
				}
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			matchingEntryName = null;
			matchingEntryData = null;
		}

		// build the response
		final ResourceResponse response = new ResourceResponse();
		response.setCacheDuration(Duration.NONE);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);
		if (matchingEntryData == null) {
			response.setFileName("plugin-bundle-" + pluginBundleId + ".jar");
		} else {
			final int lastSlashIndex = matchingEntryName.lastIndexOf('/');
			if (lastSlashIndex != -1) {
				matchingEntryName = matchingEntryName.substring(lastSlashIndex + 1);
			}
			response.setFileName(matchingEntryName);
		}
		response.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(final Attributes attributes) throws IOException {
				attributes.getResponse().write(matchingEntryData == null ? jarData : matchingEntryData);
			}
		});
		return response;

	}

}
