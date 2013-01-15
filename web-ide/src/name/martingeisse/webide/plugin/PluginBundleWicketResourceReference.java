/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * A Wicket resource reference for workspace resources.
 */
public final class PluginBundleWicketResourceReference extends ResourceReference {

	/**
	 * Constructor.
	 * @param pluginBundleId the plugin bundle ID
	 */
	public PluginBundleWicketResourceReference(final long pluginBundleId) {
		super(PluginBundleWicketResourceReference.class, Long.toString(pluginBundleId));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.ResourceReference#getResource()
	 */
	@Override
	public IResource getResource() {
		return new PluginBundleWicketResource(Long.parseLong(getName()));
	}

}
