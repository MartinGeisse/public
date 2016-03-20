/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.util;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class packages an {@link InputStream} and an
 * {@link OutputStream} together.
 */
public final class BidirectionalStreams {

	/**
	 * the STANDARD_IO
	 */
	public static final BidirectionalStreams STANDARD_IO = new BidirectionalStreams(System.in, System.out);
	
	/**
	 * the inputStream
	 */
	private final InputStream inputStream;

	/**
	 * the outputStream
	 */
	private final OutputStream outputStream;

	/**
	 * Constructor.
	 * @param inputStream the input stream
	 * @param outputStream the output stream
	 */
	public BidirectionalStreams(InputStream inputStream, OutputStream outputStream) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
	}

	/**
	 * Getter method for the inputStream.
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * Getter method for the outputStream.
	 * @return the outputStream
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

}
