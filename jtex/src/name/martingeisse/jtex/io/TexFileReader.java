/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Specialized {@link Reader} for files.
 */
public class TexFileReader extends InputStreamReader {

	/**
	 * the file
	 */
	private final File file;

	/**
	 * Constructor.
	 * @param filename the name of the file to write to
	 * @throws IOException on I/O errors
	 */
	public TexFileReader(String filename) throws IOException {
		this(new File(filename));
	}

	/**
	 * Constructor.
	 * @param file the file to write to
	 * @throws IOException on I/O errors
	 */
	public TexFileReader(File file) throws IOException {
		super(new FileInputStream(file), "utf-8");
		this.file = file;
	}

	/**
	 * Getter method for the file.
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	
}
