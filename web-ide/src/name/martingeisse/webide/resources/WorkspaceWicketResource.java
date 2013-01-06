/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.io.IOException;

import name.martingeisse.webide.resources.operation.FetchSingleResourceOperation;
import name.martingeisse.webide.resources.operation.WorkspaceResourceNotFoundException;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Duration;

/**
 * A Wicket resource that represents a workspace resource.
 */
public class WorkspaceWicketResource extends AbstractResource {

	/**
	 * the path
	 */
	private final ResourcePath path;

	/**
	 * Constructor.
	 * @param path the path
	 */
	public WorkspaceWicketResource(final ResourcePath path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.AbstractResource#newResourceResponse(org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected ResourceResponse newResourceResponse(final Attributes attributes) {
		final ResourceResponse response = new ResourceResponse();
		final FetchSingleResourceOperation operation = new FetchSingleResourceOperation(path);
		try {
			operation.run();
		} catch (final WorkspaceResourceNotFoundException e) {
			response.setError(404);
			return response;
		}
		response.setCacheDuration(Duration.NONE);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);
		response.setFileName(path.getLastSegment());
		response.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(final Attributes attributes) throws IOException {
				attributes.getResponse().write(operation.getResult().getContents());
			}
		});
		return response;
	}

}
