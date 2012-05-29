/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * PNG image backend.
 */
public class PngImageBackend extends AbstractImageBackend {

	/**
	 * the bufferedImage
	 */
	private final BufferedImage bufferedImage;

	/**
	 * the graphics2d
	 */
	private Graphics2D graphics2d;

	/**
	 * Constructor.
	 * @param width the image width
	 * @param height the image height
	 * @param alpha whether the image has an alpha channel
	 */
	public PngImageBackend(final int width, final int height, final boolean alpha) {
		this.bufferedImage = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		this.graphics2d = bufferedImage.createGraphics();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.image.IImageBackend#getGraphics()
	 */
	@Override
	public Graphics2D getGraphics() throws IllegalStateException {
		mustNotBeFinished();
		return graphics2d;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.image.IImageBackend#finish()
	 */
	@Override
	public IImageBackend finish() {
		mustNotBeFinished();
		graphics2d.dispose();
		graphics2d = null;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.image.IImageBackend#writeTo(java.io.OutputStream)
	 */
	@Override
	public void writeTo(final OutputStream s) throws IOException {
		mustBeFinished();
		ImageIO.write(bufferedImage, "png", s);
	}

	/**
	 * Throws an exception if this backend is already finished.
	 */
	private void mustNotBeFinished() {
		if (graphics2d == null) {
			throw new IllegalStateException("backend is already finished");
		}
	}

	/**
	 * Throws an exception if this backend is not yet finished.
	 */
	private void mustBeFinished() {
		if (graphics2d != null) {
			throw new IllegalStateException("backend is not yet finished");
		}
	}

}
