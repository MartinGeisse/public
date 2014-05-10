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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * This class wraps a servlet output stream an applies GZIP compression to the content.
 * Zipped output is buffered to provide a valid Content-Length to the client.
 */
public class GzipServletOutputStream extends ServletOutputStream {
	
	/**
	 * the response
	 */
	protected HttpServletResponse response;
	
	/**
	 * the originalOutputStream
	 */
	protected ServletOutputStream originalOutputStream;
	
	/**
	 * the byteArrayOutputStream
	 */
	protected ByteArrayOutputStream byteArrayOutputStream;
	
	/**
	 * the gzipStream
	 */
	protected GZIPOutputStream gzipStream;
	
	/**
	 * the closed
	 */
	protected boolean closed;
	
	/**
	 * the finalData
	 */
	protected byte[] finalData;

	/**
	 * Constructor.
	 * @param response the servlet response
	 * @throws IOException on I/O errors
	 */
	public GzipServletOutputStream(final HttpServletResponse response) throws IOException {
		super();
		this.response = response;
		this.originalOutputStream = response.getOutputStream();
		this.byteArrayOutputStream = new ByteArrayOutputStream();
		this.gzipStream = new GZIPOutputStream(byteArrayOutputStream);
		this.closed = false;
		this.finalData = null;
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		
		// closing again is allowed
		if (closed) {
			return;
		}
		
		// finish compression
		gzipStream.finish();

		// get the compressed output
		final byte[] bytes = byteArrayOutputStream.toByteArray();

		// set HTTP headers
		response.addHeader("Content-Length", Integer.toString(bytes.length));
		response.addHeader("Content-Encoding", "gzip");
		
		// send the compressed output to the client
		originalOutputStream.write(bytes);
		originalOutputStream.flush();
		originalOutputStream.close();
		
		// mark this stream as closed
		closed = true;
		finalData = bytes;
		
	}
	
	/**
	 * Throws an exception if this stream has already been closed.
	 */
	private void checkClosed() throws IOException {
		if (closed) {
			throw new IOException("this stream has already been closed");
		}
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		checkClosed();
		gzipStream.flush();
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public void write(final int b) throws IOException {
		checkClosed();
		gzipStream.write(b);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(final byte b[]) throws IOException {
		checkClosed();
		gzipStream.write(b);
	}

	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(final byte b[], final int off, final int len) throws IOException {
		checkClosed();
		gzipStream.write(b, off, len);
	}

	/**
	 * Getter method for the finalData.
	 * @return the finalData
	 */
	public byte[] getFinalData() {
		return finalData;
	}
	
}
