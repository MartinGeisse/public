/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.image;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This renderer simply creates an image that is 100x100 and all blue.
 */
public class TestRenderer implements IImageRenderer {

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.image.IImageRenderer#render(name.martingeisse.terra.image.IImageBackendType)
	 */
	@Override
	public IImageBackend render(IImageBackendType imageBackendType) {
		IImageBackend backend = imageBackendType.createBackend(100, 100);
		Graphics2D graphics = backend.getGraphics();
		graphics.setColor(Color.BLUE);
		graphics.fillRect(0, 0, 100, 100);
		return backend;
	}

}
