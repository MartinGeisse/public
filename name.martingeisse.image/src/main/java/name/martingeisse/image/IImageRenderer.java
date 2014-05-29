/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.image;

/**
 * This interface is implemented by all image renderers. Implementations are able,
 * given an {@link IImageBackendType}, to generate a finished {@link IImageBackend}.
 * The size and contents of that backend are implementation-specific.
 */
public interface IImageRenderer {

	/**
	 * Renders an image.
	 * @param imageBackendType the backend type that defines the technical nature of the image
	 * @return the rendered and finished image
	 */
	public IImageBackend render(IImageBackendType imageBackendType);
	
}
