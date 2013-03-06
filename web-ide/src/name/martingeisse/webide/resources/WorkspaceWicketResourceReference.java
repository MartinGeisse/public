/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * A Wicket resource reference for workspace resources.
 */
public final class WorkspaceWicketResourceReference extends ResourceReference {

	/**
	 * Constructor.
	 * @param resourceHandle the resource handle
	 */
	public WorkspaceWicketResourceReference(final ResourceHandle resourceHandle) {
		super(WorkspaceWicketResourceReference.class, Long.toString(resourceHandle.getWorkspaceId()) + resourceHandle.getPath().toString());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.ResourceReference#getResource()
	 */
	@Override
	public IResource getResource() {
		ResourcePath pseudoPath = new ResourcePath(getName());
		long workspaceId = Long.parseLong(pseudoPath.getFirstSegment());
		ResourcePath path = pseudoPath.removeFirstSegment(true);
		return new WorkspaceWicketResource(new ResourceHandle(workspaceId, path));
	}

}
