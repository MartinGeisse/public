/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.io;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Specialized {@link DataOutputStream} for files.
 */
public final class TexFileDataOutputStream extends DataOutputStream {

	/**
	 * the file
	 */
	private final File file;

	/**
	 * Constructor.
	 * @param filename the name of the file to write to
	 * @throws IOException on I/O errors
	 */
	public TexFileDataOutputStream(String filename) throws IOException {
		this(new File(filename));
	}

	/**
	 * Constructor.
	 * @param file the file to write to
	 * @throws IOException on I/O errors
	 */
	public TexFileDataOutputStream(File file) throws IOException {
		super(new FileOutputStream(file));
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
	 * @see java.io.FilterOutputStream#close()
	 */
	@Override
	public void close() {
		try {
			super.close();
		} catch (IOException e) {
			System.err.println("Could not close output stream for file: " + file.getPath());
		}
	}
	
}
