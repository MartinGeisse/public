/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.io.IOException;

import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Duration;

/**
 * A Wicket resource that represents a workspace resource.
 */
public class WorkspaceWicketResource extends AbstractResource {

	/**
	 * the filename
	 */
	private final String filename;

	/**
	 * Constructor.
	 * @param filename the filename
	 */
	public WorkspaceWicketResource(final String filename) {
		this.filename = filename;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.AbstractResource#newResourceResponse(org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes) {
		final byte[] contents = WorkspaceUtil.getContents(filename);
		ResourceResponse response = new ResourceResponse();
		response.setCacheDuration(Duration.NONE);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);
		response.setFileName(filename);
		if (contents == null) {
			response.setError(404);
			return response;
		}
		response.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(Attributes attributes) throws IOException {
				attributes.getResponse().write(contents);
			}
		});
		return response;
	}

}
