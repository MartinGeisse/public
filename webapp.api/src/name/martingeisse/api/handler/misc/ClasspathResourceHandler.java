/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.handler.misc;

import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import name.martingeisse.api.handler.ISelfDescribingRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

import org.apache.commons.io.IOUtils;

/**
 * This handler writes the contents of a classpath resource
 * to the response. 
 * 
 * The Content-Type must be specified since it cannot be detected
 * automatically. The characterEncoding must be specified for
 * text types.
 * 
 * The classpath scope is a class and determines the classpath
 * package/folder in which the resource is located as well as the
 * class loader used to load the resource. If it is null then the
 * class of this handler is used. This is only useful for
 * subclasses of this class since there are no classpath resource
 * in this package.
 *
 * The filename must be specified.
 */
public class ClasspathResourceHandler implements ISelfDescribingRequestHandler {

	/**
	 * the contentType
	 */
	private final String contentType;
	
	/**
	 * the characterEncoding
	 */
	private final String characterEncoding;

	/**
	 * the classpathScope
	 */
	private final Class<?> classpathScope;

	/**
	 * the classpathFilename
	 */
	private final String classpathFilename;

	/**
	 * Constructor.
	 * @param contentType the Content-Type of the resource
	 * @param classpathScope the scope class that determines the package to load the
	 * resource from
	 * @param classpathFilename the filename of the resource in the classpath
	 */
	public ClasspathResourceHandler(final String contentType, final Class<?> classpathScope, final String classpathFilename) {
		this.contentType = contentType;
		this.characterEncoding = null;
		this.classpathScope = classpathScope;
		this.classpathFilename = classpathFilename;
	}

	/**
	 * Constructor.
	 * @param contentType the Content-Type of the resource
	 * @param characterEncoding the character encoding (only needed for text types)
	 * @param classpathScope the scope class that determines the package to load the
	 * resource from
	 * @param classpathFilename the filename of the resource in the classpath
	 */
	public ClasspathResourceHandler(final String contentType, final String characterEncoding, final Class<?> classpathScope, final String classpathFilename) {
		this.contentType = contentType;
		this.characterEncoding = characterEncoding;
		this.classpathScope = classpathScope;
		this.classpathFilename = classpathFilename;
	}

	/**
	 * Getter method for the contentType.
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Getter method for the classpathScope.
	 * @return the classpathScope
	 */
	public Class<?> getClasspathScope() {
		return classpathScope;
	}
	
	/**
	 * Getter method for the classpathFilename.
	 * @return the classpathFilename
	 */
	public String getClasspathFilename() {
		return classpathFilename;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(final RequestCycle requestCycle, final RequestPathChain path) throws Exception {
		HttpServletResponse response = requestCycle.getResponse();
		
		// set headers
		if (characterEncoding != null) {
			response.setCharacterEncoding(characterEncoding);
		}
		response.setContentType(contentType);
		
		// write the resource to the response body
		Class<?> scope = (this.classpathScope == null ? getClass() : this.classpathScope);
		String packageName = scope.getPackage().getName();
		String internalPath = "/" + packageName.replace('.', '/') + '/' + classpathFilename;
		InputStream inputStream = scope.getResourceAsStream(internalPath);
		if (inputStream == null) {
			throw new Exception("classpath resource not found: " + internalPath);
		}
		OutputStream outputStream = requestCycle.getOutputStream();
		IOUtils.copy(inputStream, outputStream);
		inputStream.close();
		outputStream.flush();
		outputStream.close();

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.ISelfDescribingRequestHandler#getShortDescription()
	 */
	@Override
	public String getShortDescription() {
		return "file";
	}

}
