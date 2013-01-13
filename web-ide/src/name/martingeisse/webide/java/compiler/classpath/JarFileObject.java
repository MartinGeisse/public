/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler.classpath;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.jar.JarEntry;

import javax.tools.FileObject;

import org.apache.commons.io.IOUtils;

/**
 * {@link FileObject} implementation based on JAR entries.
 */
public class JarFileObject implements FileObject {

	/**
	 * the fileManager
	 */
	private final JarFileManager fileManager;
	
	/**
	 * the jarEntry
	 */
	private final JarEntry jarEntry;

	/**
	 * Constructor.
	 * @param fileManager the file manager that owns this file
	 * @param jarEntry the JAR entry to wrap
	 */
	public JarFileObject(final JarFileManager fileManager, final JarEntry jarEntry) {
		this.fileManager = fileManager;
		this.jarEntry = jarEntry;
	}
	
	/**
	 * Getter method for the fileManager.
	 * @return the fileManager
	 */
	public JarFileManager getFileManager() {
		return fileManager;
	}
	
	/**
	 * Getter method for the jarEntry.
	 * @return the jarEntry
	 */
	public JarEntry getJarEntry() {
		return jarEntry;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#delete()
	 */
	@Override
	public boolean delete() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getCharContent(boolean)
	 */
	@Override
	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
		Reader r = openReader(ignoreEncodingErrors);
		String content = IOUtils.toString(r);
		r.close();
		return content;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getLastModified()
	 */
	@Override
	public long getLastModified() {
		return jarEntry.getTime();
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getName()
	 */
	@Override
	public String getName() {
		return jarEntry.getName();
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws IOException {
		return fileManager.getJarFile().getInputStream(jarEntry);
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openOutputStream()
	 */
	@Override
	public OutputStream openOutputStream() throws IOException {
		throw new IllegalStateException("writing to library class files not supported");
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openReader(boolean)
	 */
	@Override
	public Reader openReader(final boolean ignoreEncodingErrors) throws IOException {
		return new InputStreamReader(openInputStream(), Charset.forName("utf-8"));
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openWriter()
	 */
	@Override
	public Writer openWriter() throws IOException {
		throw new IllegalStateException("writing to library class files not supported");
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#toUri()
	 */
	@Override
	public URI toUri() {
		try {
			return new URI("library", null, "/" + getName(), null);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
