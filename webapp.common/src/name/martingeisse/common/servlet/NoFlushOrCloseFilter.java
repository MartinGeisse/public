/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Prevents flushing or closing the response.
 */
public class NoFlushOrCloseFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (response instanceof HttpServletResponse) {
			chain.doFilter(request, new MyResponseWrapper((HttpServletResponse)response));
		} else {
			chain.doFilter(request, response);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
	}

	/**
	 *
	 */
	static final class MyResponseWrapper extends HttpServletResponseWrapper {

		/**
		 * the servletOutputStreamWrapper
		 */
		private ServletOutputStreamWrapper servletOutputStreamWrapper;
		
		/**
		 * the printWriterWrapper
		 */
		private PrintWriterWrapper printWriterWrapper;
		
		/**
		 * Constructor.
		 * @param response the response to wrap
		 */
		public MyResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletResponseWrapper#flushBuffer()
		 */
		@Override
		public void flushBuffer() throws IOException {
		}

		/* (non-Javadoc)
		 * @see javax.servlet.ServletResponseWrapper#getOutputStream()
		 */
		@Override
		public ServletOutputStream getOutputStream() throws IOException {
			if (servletOutputStreamWrapper == null) {
				servletOutputStreamWrapper = new ServletOutputStreamWrapper(super.getOutputStream());
			}
			return servletOutputStreamWrapper;
		}
		
		/* (non-Javadoc)
		 * @see javax.servlet.ServletResponseWrapper#getWriter()
		 */
		@Override
		public PrintWriter getWriter() throws IOException {
			if (printWriterWrapper == null) {
				printWriterWrapper = new PrintWriterWrapper(super.getWriter());
			}
			return printWriterWrapper;
		}
		
	}
	
	/**
	 *
	 */
	static final class ServletOutputStreamWrapper extends ServletOutputStream {

		/**
		 * the wrapped
		 */
		private final ServletOutputStream wrapped;
		
		/**
		 * Constructor.
		 * @param wrapped the wrapped stream
		 */
		public ServletOutputStreamWrapper(final ServletOutputStream wrapped) {
			this.wrapped = wrapped;
		}

		/* (non-Javadoc)
		 * @see java.io.OutputStream#close()
		 */
		@Override
		public void close() throws IOException {
		}

		/* (non-Javadoc)
		 * @see java.io.OutputStream#flush()
		 */
		@Override
		public void flush() throws IOException {
		}

		/* (non-Javadoc)
		 * @see java.io.OutputStream#write(byte[], int, int)
		 */
		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			wrapped.write(b, off, len);
		}

		/* (non-Javadoc)
		 * @see java.io.OutputStream#write(byte[])
		 */
		@Override
		public void write(byte[] b) throws IOException {
			wrapped.write(b);
		}

		/* (non-Javadoc)
		 * @see java.io.OutputStream#write(int)
		 */
		@Override
		public void write(int b) throws IOException {
			wrapped.write(b);
		}

	}
	
	/**
	 *
	 */
	static final class PrintWriterWrapper extends PrintWriter {

		/**
		 * Constructor.
		 * @param writer the wrapped writer
		 */
		public PrintWriterWrapper(Writer writer) {
			super(writer);
		}
		
	}
}
