/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Specialized {@link DataInputStream} for files.
 */
public class TexFileDataInputStream extends DataInputStream {

	/**
	 * the file
	 */
	private final File file;

	/**
	 * Constructor.
	 * @param filename the name of the file to read from
	 * @throws IOException on I/O errors
	 */
	public TexFileDataInputStream(String filename) throws IOException {
		this(new File(filename));
	}
	
	/**
	 * Constructor.
	 * @param file the file to read from
	 * @throws IOException on I/O errors
	 */
	public TexFileDataInputStream(File file) throws IOException {
		super(new FileInputStream(file));
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
			System.err.println("Could not close input stream for file: " + file.getPath());
		}
	}
	
}
