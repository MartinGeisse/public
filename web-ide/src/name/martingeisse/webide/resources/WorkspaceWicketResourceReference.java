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
	 * @param filename the workspace filename
	 */
	public WorkspaceWicketResourceReference(final String filename) {
		super(WorkspaceWicketResourceReference.class, filename);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.ResourceReference#getResource()
	 */
	@Override
	public IResource getResource() {
		return new WorkspaceWicketResource(new ResourcePath(getName()));
	}

}
