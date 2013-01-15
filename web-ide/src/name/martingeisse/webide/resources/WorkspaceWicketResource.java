/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarOutputStream;

import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.FetchSingleResourceOperation;
import name.martingeisse.webide.resources.operation.RecursiveResourceOperation;
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

		// prepare the response
		final ResourceResponse response = new ResourceResponse();
		response.setCacheDuration(Duration.NONE);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);

		// fetch root resource
		final FetchSingleResourceOperation operation = new FetchSingleResourceOperation(path);
		try {
			operation.run();
		} catch (final WorkspaceResourceNotFoundException e) {
			response.setError(404);
			return response;
		}

		final byte[] downloadData;
		if (operation.getResult().getType() == ResourceType.FILE) {
			downloadData = operation.getResult().getContents();
			response.setFileName(path.getLastSegment());
		} else {
			try {
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final JarOutputStream jarOutputStream = new JarOutputStream(byteArrayOutputStream);
				new RecursiveResourceOperation(path) {
					@Override
					protected void onLevelFetched(final List<FetchResourceResult> fetchResults) {
						// TODO this method
						// TODO handle "ZIP file must have at least one entry"
					}
				}.run();
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
