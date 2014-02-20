/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Specialized {@link PrintWriter} for files.
 */
public class TexFilePrintWriter extends PrintWriter {

	/**
	 * the file
	 */
	private final File file;

	/**
	 * Constructor.
	 * @param filename the name of the file to write to
	 * @throws IOException on I/O errors
	 */
	public TexFilePrintWriter(String filename) throws IOException {
		this(new File(filename));
	}

	/**
	 * Constructor.
	 * @param file the file to write to
	 * @throws IOException on I/O errors
	 */
	public TexFilePrintWriter(File file) throws IOException {
		super(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
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
