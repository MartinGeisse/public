/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.image;

import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/**
 * PNG image backend.
 */
public class PngImageBackend extends BufferedImageBackend {

	/**
	 * Constructor.
	 * @param width the image width
	 * @param height the image height
	 * @param alpha whether the image has an alpha channel
	 */
	public PngImageBackend(final int width, final int height, final boolean alpha) {
		super(width, height, alpha);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.image.IImageBackend#writeTo(java.io.OutputStream)
	 */
	@Override
	public void writeTo(final OutputStream s) throws IOException {
		mustBeFinished();
		ImageIO.write(getBufferedImage(), "png", s);
	}

}
