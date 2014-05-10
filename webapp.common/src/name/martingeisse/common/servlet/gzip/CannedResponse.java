/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.servlet.gzip;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class wraps all data needed to respond to a request whose previous
 * response was GZIPed and cached. In other words, it represents a value
 * in the GZIP cache.
 */
final class CannedResponse {

	/**
	 * the data
	 */
	private byte[] data;

	/**
	 * the contentType
	 */
	private String contentType;

	/**
	 * the lastModified
	 */
	private String lastModified;

	/**
	 * Constructor.
	 */
	public CannedResponse() {
	}

	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Setter method for the data.
	 * @param data the data to set
	 */
	public void setData(final byte[] data) {
		this.data = data;
	}

	/**
	 * Getter method for the contentType.
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Setter method for the contentType.
	 * @param contentType the contentType to set
	 */
	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Getter method for the lastModified.
	 * @return the lastModified
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * Setter method for the lastModified.
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(final String lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Responds to the specified request.
	 * @param request the request
	 * @param response the response
	 * @throws IOException on I/O errors
	 */
	public void respond(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// headers
		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);
		response.setContentLength(data.length);
		response.setHeader("Last-Modified", lastModified);
		response.setHeader("Cache-Control", "max-age=31536000"); // 365 * 24 * 60 * 60
		response.setDateHeader("Expires", new Date().getTime() + 31536000000L); // milliseconds
		response.setHeader("Content-Encoding", "gzip");
		// response.setHeader("Terra-Note", "canned response from GZIP cache");
		
		// content
		try (OutputStream out = response.getOutputStream()) {
			out.write(data);
			out.flush();
		}
		
	}
	
}
