/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.io.IOException;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QPluginBundles;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Duration;

import com.mysema.query.sql.SQLQuery;

/**
 * A Wicket resource that represents a workspace resource.
 */
public class PluginBundleWicketResource extends AbstractResource {

	/**
	 * the pluginBundleId
	 */
	private final long pluginBundleId;

	/**
	 * Constructor.
	 * @param pluginBundleId the plugin bundle ID
	 */
	public PluginBundleWicketResource(final long pluginBundleId) {
		this.pluginBundleId = pluginBundleId;
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

		// build the response
		final ResourceResponse response = new ResourceResponse();
		response.setCacheDuration(Duration.NONE);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);
		response.setFileName("plugin-bundle-" + pluginBundleId + ".jar");
		response.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(final Attributes attributes) throws IOException {
				attributes.getResponse().write(jarData);
			}
		});
		return response;
		
	}

}
