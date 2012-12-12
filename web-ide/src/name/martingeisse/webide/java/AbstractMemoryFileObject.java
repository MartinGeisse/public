/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java;

import java.net.URI;
import java.net.URISyntaxException;

import javax.tools.FileObject;

/**
 * Base class for in-memory implementations of {@link FileObject}.
 */
public abstract class AbstractMemoryFileObject implements IMemoryFileObject {

	/**
	 * the name
	 */
	private String name;

	/**
	 * Constructor.
	 * @param name the file name
	 */
	public AbstractMemoryFileObject(final String name) {
		this.name = name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#delete()
	 */
	@Override
	public boolean delete() {
		return false;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getLastModified()
	 */
	@Override
	public long getLastModified() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see javax.tools.FileObject#toUri()
	 */
	@Override
	public URI toUri() {
		try {
			return new URI("webide", "/" + name, null);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
