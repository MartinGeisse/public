/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.application;

import name.martingeisse.webide.plugin.PluginBundleWicketResourceReference;
import name.martingeisse.webide.resources.ResourceHandle;
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
			ResourcePath pseudoPath = new ResourcePath(key.getName());
			long workspaceId = Long.parseLong(pseudoPath.getFirstSegment());
			ResourcePath path = pseudoPath.removeFirstSegment(true);
			return new WorkspaceWicketResourceReference(new ResourceHandle(workspaceId, path));
		}
		if (key.getScope().equals(PluginBundleWicketResourceReference.class.getName())) {
			return new PluginBundleWicketResourceReference(key.getName());
		}
		return super.createDefaultResourceReference(key);
	}

}
