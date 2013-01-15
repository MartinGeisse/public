/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler.classpath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import javax.tools.FileObject;

import org.apache.commons.io.IOUtils;

/**
 * Read-only {@link FileObject} implementation based on regular files.
 */
public class ReadOnlyRegularFileObject implements FileObject {

	/**
	 * the file
	 */
	private final File file;

	/**
	 * Constructor.
	 * @param file the file
	 */
	public ReadOnlyRegularFileObject(final File file) {
		this.file = file;
	}

	/**
	 * Getter method for the file.
	 * @return the file
	 */
	public File getFile() {
		return file;
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
		final Reader r = openReader(ignoreEncodingErrors);
		final String content = IOUtils.toString(r);
		r.close();
		return content;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getLastModified()
	 */
	@Override
	public long getLastModified() {
		return file.lastModified();
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getName()
	 */
	@Override
	public String getName() {
		return file.getPath();
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws IOException {
		return new FileInputStream(file);
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
			return new URI("library", null, getName(), null);
		} catch (final URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{file " + file.getPath() + "}";
	}
	
}
