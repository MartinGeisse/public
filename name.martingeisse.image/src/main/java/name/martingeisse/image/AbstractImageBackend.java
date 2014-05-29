/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Abstract {@link IImageBackend} implementation that provides the utility
 * methods of that interface.
 */
public abstract class AbstractImageBackend implements IImageBackend {

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.image.IImageBackend#writeToByteArray()
	 */
	@Override
	public byte[] writeToByteArray() throws IOException {
		ByteArrayOutputStream s = new ByteArrayOutputStream();
		writeTo(s);
		return s.toByteArray();
	}
	
}
