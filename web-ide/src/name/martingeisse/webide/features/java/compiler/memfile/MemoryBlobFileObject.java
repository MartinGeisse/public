/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.compiler.memfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.tools.FileObject;

/**
 * Byte array based in-memory implementation of {@link FileObject}.
 * 
 * TODO: ignoreEncodingErrors
 */
public class MemoryBlobFileObject extends AbstractMemoryFileObject {

	/**
	 * the CHARSET
	 */
	private static Charset CHARSET = Charset.forName("utf-8");
	
	/**
	 * the contents
	 */
	private byte[] contents;

	/**
	 * Constructor.
	 * @param name the file name
	 */
	public MemoryBlobFileObject(final String name) {
		super(name);
	}

	/**
	 * Constructor.
	 * @param name the file name
	 * @param contents the contents
	 */
	public MemoryBlobFileObject(final String name, final byte[] contents) {
		super(name);
		this.contents = contents;
	}

	/**
	 * Constructor.
	 * @param name the file name
	 * @param contents the contents
	 */
	public MemoryBlobFileObject(final String name, final String contents) {
		super(name);
		this.contents = contents.getBytes(CHARSET);
	}
	
	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getCharContent(boolean)
	 */
	@Override
	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
		return new String(contents, CHARSET);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.java.IMemoryFileObject#getBinaryContent()
	 */
	@Override
	public byte[] getBinaryContent() {
		return contents;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws IOException {
		return new ByteArrayInputStream(contents);
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openOutputStream()
	 */
	@Override
	public OutputStream openOutputStream() throws IOException {
		return new ByteArrayOutputStream() {
			@Override
			public void close() throws IOException {
				contents = toByteArray();
				super.close();
			}
		};
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openReader(boolean)
	 */
	@Override
	public Reader openReader(final boolean ignoreEncodingErrors) throws IOException {
		return new InputStreamReader(openInputStream(), CHARSET);
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openWriter()
	 */
	@Override
	public Writer openWriter() throws IOException {
		return new OutputStreamWriter(openOutputStream(), CHARSET);
	}

}
