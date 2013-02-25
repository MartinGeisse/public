/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FileUtils;
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

		// prepare the response
		final ResourceResponse response = new ResourceResponse();
		response.setCacheDuration(Duration.NONE);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);

		// fetch root resource
		File resource = Workspace.map(path);
		if (!resource.exists() || (!resource.isFile() && !resource.isDirectory())) {
			response.setError(404);
			return response;
		}

		final byte[] downloadData;
		if (resource.isFile()) {
			try {
				downloadData = FileUtils.readFileToByteArray(resource);
				response.setFileName(path.getLastSegment());
			} catch (IOException e) {
				response.setError(500);
				return response;
			}
		} else {
			try {
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final JarOutputStream jarOutputStream = new JarOutputStream(byteArrayOutputStream);
				new RecursiveResourceOperation() {
					// TODO implement
					// TODO handle "ZIP file must have at least one entry"
				}.handle(resource);
				jarOutputStream.close();
				downloadData = byteArrayOutputStream.toByteArray();
				response.setFileName(path.getSegmentCount() == 0 ? "workspace.zip" : (path.getLastSegment() + ".zip"));
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}

		// build a write callback from the data
		response.setWriteCallback(new WriteCallback() {
			@Override
			public void writeData(final Attributes attributes) throws IOException {
				attributes.getResponse().write(downloadData);
			}
		});

		return response;
	}

}
