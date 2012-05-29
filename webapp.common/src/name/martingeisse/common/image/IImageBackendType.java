/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.image;

/**
 * Implementations of this interface act as factories for {@link IImageBackend}
 * objects. Most implementations are provided as singleton objects.
 * 
 * The image backend type determines the nature of the generated image. For example,
 * it defines whether a pixel-based or vector-based image is generated. It may also
 * define the format in which the image will be saved (if at all). It usually doesn't
 * determine how the image is used, or where it is saved / sent. This is specified
 * in the corresponding methods of {@link IImageBackend}.
 */
public interface IImageBackendType {

	/**
	 * Creates an image backend.
	 * 
	 * The width and height parameters serve two different but related purposes:
	 * Regardless of the backend type, they define the coordinate system of the
	 * image backend's graphics context. The upper-left corner of the generated
	 * image is at (0, 0); the lower-right corner is at (width, height).
	 * 
	 * The second purpose only applies to pixel-based backends. For those, the
	 * width and height arguments define the resolution of the image.
	 * 
	 * Clients who wish to separate the resolution from the coordinate system are
	 * free to set a different transformation in the graphics context; the widht and
	 * height arguments only specify the initial transformation.
	 * 
	 * @param width the width of the image backend in both coordinates and pixels (if applicable)
	 * @param height the height of the image backend in both coordinates and pixels (if applicable)
	 * @return the image backend
	 */
	public IImageBackend createBackend(int width, int height);
	
}
