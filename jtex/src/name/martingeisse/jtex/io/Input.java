/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Wraps a reader to convert exceptions and provide line reading.
 */
public final class Input implements Closeable {

	private final String name;
	private final LineNumberReader reader;

	/**
	 * Constructor.
	 * @param name the name (for error output)
	 * @param reader the reader
	 */
	private Input(final String name, final Reader reader) {
		this.name = name;
		this.reader = new LineNumberReader(new BufferedReader(reader));
	}

	/**
	 * Creates an instance for the specified reader.
	 * 
	 * @param name the name (for error output)
	 * @param reader the reader
	 * @return the instance
	 */
	public static Input from(final String name, final Reader reader) {
		return new Input(name, reader);
	}

	/**
	 * Creates an instance for the specified file.
	 * 
	 * @param file the file to read
	 * @return the instance
	 */
	public static Input from(final File file) {
		try {
			return new Input(file.getPath(), new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new RuntimeException("cannot open file: " + file.getPath(), e);
		}
	}

	/**
	 * Reads a single character.
	 *
	 * @return the character read, or -1 if at end of file
	 */
	public int readCharacter() {
		try {
			return reader.read();
		} catch (final IOException e) {
			throw new RuntimeException("cannot read from " + name, e);
		}
	}

	/**
	 * Reads (the remainder of) a line.
	 *
	 * @return the line, or null if at end of file
	 */
	public String readLine() {
		try {
			return reader.readLine();
		} catch (final IOException e) {
			throw new RuntimeException("cannot read from " + name, e);
		}
	}

	// override
	@Override
	public void close() {
		try {
			reader.close();
		} catch (final IOException e) {
			throw new RuntimeException("cannot close reader for " + name, e);
		}
	}
	
}
