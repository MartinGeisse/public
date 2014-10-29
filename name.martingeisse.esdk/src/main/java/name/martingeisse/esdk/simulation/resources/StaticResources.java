/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.simulation.resources;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Contains the "static" resources such as image files, sound files, etc.
 */
public final class StaticResources {

	/**
	 * the instance
	 */
	public static StaticResources instance = null;

	/**
	 * Loads the static LWJGL resources. Does nothing if already loaded.
	 */
	public static void load() {
		if (instance == null) {
			try {
				instance = new StaticResources();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Obtains the shared instance of this class.
	 * @return the shared instance
	 */
	public static StaticResources getInstance() {
		if (instance == null) {
			throw new IllegalStateException("static resources not yet loaded");
		}
		return instance;
	}

	/**
	 * the font
	 */
	private final Font font;

	/**
	 * Constructor.
	 * @throws IOException on I/O errors
	 */
	public StaticResources() throws IOException {
		font = new FixedWidthFont(loadImage("font.png"), 8, 16);
	}

	/**
	 * Loads a PNG image the AWT way.
	 * @param filename the filename of the PNG, relative to the assets folder
	 * @return the luminance buffer
	 * @throws IOException on I/O errors
	 */
	private BufferedImage loadImage(final String filename) throws IOException {
		try (InputStream inputStream = StaticResources.class.getResourceAsStream(filename)) {
			if (inputStream == null) {
				throw new FileNotFoundException("StaticResources: " + filename);
			}
			return ImageIO.read(inputStream);
		}
	}

	/**
	 * Getter method for the font.
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

}
