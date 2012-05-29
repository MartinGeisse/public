/*
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com)
 * This code is from "Servlets and JavaServer pages; the J2EE Web Tier",
 * http://www.jspbook.com. You may freely use the code both commercially
 * and non-commercially. If you like the code, please pick up a copy of
 * the book and help support the authors, development of more free code,
 * and the JSP/Servlet/J2EE community.
 * 
 * Modifications made by Martin Geisse to adapt this code to the
 * Terra framework, Copyright (c) 2011 Martin Geisse
 */
package name.martingeisse.common.servlet.gzip;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * This response GZIPs the output.
 */
public class GzipResponseWrapper extends HttpServletResponseWrapper {
	
	/**
	 * the stream
	 */
	protected GzipServletOutputStream stream;
	
	/**
	 * the writer
	 */
	protected PrintWriter writer;
	
	/**
	 * the cacheable
	 */
	protected boolean cacheable;
	
	/**
	 * the lastModified
	 */
	private String lastModified;

	/**
	 * Constructor.
	 * @param originalResponse the original response
	 */
	public GzipResponseWrapper(final HttpServletResponse originalResponse) {
		super(originalResponse);
		stream = null;
		writer = null;
		cacheable = false;
		lastModified = null;
	}

	/**
	 * @throws IOException on I/O errors
	 */
	private void createOutputStream() throws IOException {
		stream = new GzipServletOutputStream((HttpServletResponse)getResponse());
	}
	
	/**
	 * @throws IOException on I/O errors
	 */
	private void createWriter() throws IOException {
		writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
	}
	
	/**
	 * 
	 */
	public void finishResponse() {
		try {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (stream != null) {
				stream.flush();
				stream.close();
			}
		} catch (final IOException e) {
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#flushBuffer()
	 */
	@Override
	public void flushBuffer() throws IOException {
		if (writer != null) {
			writer.flush();
		}
		if (stream != null) {
			stream.flush();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
	 */
	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		
		// this method must not be used if getWriter() has been called before
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called!");
		}

		// create the stream if needed
		if (stream == null) {
			createOutputStream();
		}
		
		return stream;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#getWriter()
	 */
	@Override
	public PrintWriter getWriter() throws IOException {
		
		// if the writer already exists, just return it
		if (writer != null) {
			return writer;
		}

		// otherwise, if a stream exists, getOutputStream() has been called, so getWriter() is disallowed
		if (stream != null) {
			throw new IllegalStateException("getOutputStream() has already been called!");
		}

		// create stream and writer
		createOutputStream();
		createWriter();
		
		return writer;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletResponseWrapper#setContentLength(int)
	 */
	@Override
	public void setContentLength(final int length) {
		// don't allow callers to set the Content-Length header since they cannot know the final value
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void setHeader(String name, String value) {
		super.setHeader(name, value);
		watchHeader(name, value);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
	 */
	@Override
	public void addHeader(String name, String value) {
		super.addHeader(name, value);
		watchHeader(name, value);
	}
	
	/**
	 * @param name
	 * @param value
	 */
	private void watchHeader(String name, String value) {
		if (name.equalsIgnoreCase("Cache-Control")) {
			cacheable = false;
			String prefix = "max-age=";
			if (value.startsWith(prefix)) {
				try {
					int maxAge = Integer.parseInt(value.substring(prefix.length()));
					cacheable = (maxAge > (300 * 24 * 60 * 60));
				} catch (NumberFormatException e) {
				}
			}
		} else if (name.equalsIgnoreCase("Last-Modified")) {
			this.lastModified = value;
		}
	}
	
	/**
	 * Getter method for the cacheable.
	 * @return the cacheable
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * @return a {@link CannedResponse} with data from this response
	 */
	CannedResponse createCannedResponse() {
		CannedResponse cannedResponse = new CannedResponse();
		cannedResponse.setData(stream.getFinalData());
		cannedResponse.setContentType(getContentType());
		cannedResponse.setLastModified(lastModified);
		return cannedResponse;
	}
	
}
