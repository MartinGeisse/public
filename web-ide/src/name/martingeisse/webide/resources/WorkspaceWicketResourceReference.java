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
	 * @param path the resource path
	 */
	public WorkspaceWicketResourceReference(final ResourcePath path) {
		super(WorkspaceWicketResourceReference.class, path.withLeadingSeparator(false).toString());
		if (!path.isLeadingSeparator()) {
			throw new IllegalArgumentException("trying to build a resource reference from a relative path: [" + path.toString() + "]");
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.ResourceReference#getResource()
	 */
	@Override
	public IResource getResource() {
		return new WorkspaceWicketResource(new ResourcePath("/" + getName()));
	}

}
