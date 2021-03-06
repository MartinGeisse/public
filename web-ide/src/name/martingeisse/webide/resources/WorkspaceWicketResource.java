/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.request.Request;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.time.Duration;

/**
 * A Wicket resource that represents a workspace resource.
 */
public class WorkspaceWicketResource extends AbstractResource {

	/**
	 * the resourceHandle
	 */
	private final ResourceHandle resourceHandle;

	/**
	 * Constructor.
	 * @param resourceHandle the resource handle
	 */
	public WorkspaceWicketResource(final ResourceHandle resourceHandle) {
		this.resourceHandle = resourceHandle;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.AbstractResource#newResourceResponse(org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected ResourceResponse newResourceResponse(final Attributes attributes) {
		final Request request = attributes.getRequest();
		final HttpServletRequest httpServletRequest = (HttpServletRequest)request.getContainerRequest();
		if (httpServletRequest.getMethod().equalsIgnoreCase("put")) {
			return newPutResponse(attributes);
		} else {
			return newGetResponse(attributes);
		}
	}

	// handler method for GET requests (read the resource)
	private ResourceResponse newGetResponse(final Attributes attributes) {
		
		// prepare the response
		final ResourceResponse response = new ResourceResponse();
		response.setCacheDuration(Duration.NONE);
		response.setContentDisposition(ContentDisposition.ATTACHMENT);

		// fetch root resource
		if (!resourceHandle.exists() || (!resourceHandle.isFile() && !resourceHandle.isFolder())) {
			response.setError(404);
			return response;
		}

		final byte[] downloadData;
		if (resourceHandle.isFile()) {
			try {
				downloadData = resourceHandle.readBinaryFile(true);
				response.setFileName(resourceHandle.getPath().getLastSegment());
			} catch (WorkspaceOperationException e) {
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
				}.handle(resourceHandle);
				jarOutputStream.close();
				downloadData = byteArrayOutputStream.toByteArray();
				response.setFileName(resourceHandle.getPath().getSegmentCount() == 0 ? "workspace.zip" : (resourceHandle.getPath().getLastSegment() + ".zip"));
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

	// handler method for PUT requests (create/replace the resource)
	private ResourceResponse newPutResponse(final Attributes attributes) {
		
		// write the resource
		final Request request = attributes.getRequest();
		final HttpServletRequest httpServletRequest = (HttpServletRequest)request.getContainerRequest();
		try {
			final InputStream inputStream = httpServletRequest.getInputStream();
			resourceHandle.writeFile(inputStream, false, true);
			inputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// create the response
		final ResourceResponse response = new ResourceResponse();
		response.setCacheDuration(Duration.NONE);
		response.setStatusCode(204);
		return response;

	}
	
}
