/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.compiler.memfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.tools.FileObject;

import org.apache.commons.io.input.ReaderInputStream;
import org.eclipse.jetty.io.WriterOutputStream;

/**
 * String based in-memory implementation of {@link FileObject}.
 * 
 * TODO: ignoreEncodingErrors
 */
public class MemoryStringFileObject extends AbstractMemoryFileObject {

	/**
	 * the contents
	 */
	private String contents;

	/**
	 * Constructor.
	 * @param name the file name
	 */
	public MemoryStringFileObject(final String name) {
		super(name);
	}
	
	/**
	 * Constructor.
	 * @param name the file name
	 * @param contents the contents
	 */
	public MemoryStringFileObject(final String name, final byte[] contents) {
		super(name);
		this.contents = new String(contents, Charset.forName("utf-8"));
	}

	/**
	 * Constructor.
	 * @param name the file name
	 * @param contents the contents
	 */
	public MemoryStringFileObject(final String name, final String contents) {
		super(name);
		this.contents = contents;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getCharContent(boolean)
	 */
	@Override
	public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws IOException {
		return contents;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.java.IMemoryFileObject#getBinaryContent()
	 */
	@Override
	public byte[] getBinaryContent() {
		return contents.getBytes(Charset.forName("utf-8"));
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openInputStream()
	 */
	@Override
	public InputStream openInputStream() throws IOException {
		return new ReaderInputStream(openReader(true), "utf-8");
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openOutputStream()
	 */
	@Override
	public OutputStream openOutputStream() throws IOException {
		return new WriterOutputStream(openWriter(), "utf-8");
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openReader(boolean)
	 */
	@Override
	public Reader openReader(final boolean ignoreEncodingErrors) throws IOException {
		return new StringReader(contents);
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#openWriter()
	 */
	@Override
	public Writer openWriter() throws IOException {
		return new StringWriter() {
			/* (non-Javadoc)
			 * @see java.io.StringWriter#close()
			 */
			@Override
			public void close() throws IOException {
				contents = toString();
				super.close();
			}
		};
	}

}
