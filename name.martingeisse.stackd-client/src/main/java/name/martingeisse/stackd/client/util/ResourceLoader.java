/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * This class contains all resource loading methods.
 */
public final class ResourceLoader {

	/**
	 * Prevent instantiation.
	 */
	private ResourceLoader() {
	}
	
	/**
	 * Loads an image the AWT way.
	 * 
	 * @param anchor this class specifies the package that contains the file
	 * @param filename the filename of the image
	 * @return the image
	 */
	public static BufferedImage loadAwtImage(final Class<?> anchor, final String filename) {
		try (InputStream inputStream = anchor.getResourceAsStream(filename)) {
			return ImageIO.read(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
