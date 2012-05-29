/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.image;

/**
 * Singleton image backend type implementation for PNG images.
 */
public class AlphaPngImageBackendType implements IImageBackendType {

	/**
	 * the instance
	 */
	public static final AlphaPngImageBackendType instance = new AlphaPngImageBackendType();

	/**
	 * Constructor.
	 */
	private AlphaPngImageBackendType() {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.image.IImageBackendType#createBackend(int, int)
	 */
	@Override
	public IImageBackend createBackend(int width, int height) {
		return new PngImageBackend(width, height, true);
	}

}
