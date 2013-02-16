/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.image;

import java.awt.image.BufferedImage;

/**
 * Basic implementation of {@link IImageBackendType} using a {@link BufferedImage}
 * but without any knowledge about file formats. This class can be used to
 * obtain the image as a {@link BufferedImage} object. Any attempt to write the
 * image to a file will fail.
 */
public class BufferedImageBackendType implements IImageBackendType {

	/**
	 * the instance
	 */
	public static final BufferedImageBackendType instance = new BufferedImageBackendType();

	/**
	 * Constructor.
	 */
	private BufferedImageBackendType() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.image.IImageBackendType#createBackend(int, int)
	 */
	@Override
	public IImageBackend createBackend(int width, int height) {
		return new BufferedImageBackend(width, height, false);
	}
	
}
