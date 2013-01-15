/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.util;

import name.martingeisse.webide.plugin.PluginBundleWicketResourceReference;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.WorkspaceWicketResourceReference;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.ResourceReference.Key;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;

/**
 * Custom resource reference registry that is able create IDE-specific resource
 * references on demand.
 */
public class MyResourceReferenceRegistry extends ResourceReferenceRegistry {

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.ResourceReferenceRegistry#createDefaultResourceReference(org.apache.wicket.request.resource.ResourceReference.Key)
	 */
	@Override
	protected ResourceReference createDefaultResourceReference(final Key key) {
		if (key.getScope().equals(WorkspaceWicketResourceReference.class.getName())) {
			return new WorkspaceWicketResourceReference(new ResourcePath("/" + key.getName()));
		}
		if (key.getScope().equals(PluginBundleWicketResourceReference.class.getName())) {
			return new PluginBundleWicketResourceReference(Long.parseLong(key.getName()));
		}
		return super.createDefaultResourceReference(key);
	}

}
