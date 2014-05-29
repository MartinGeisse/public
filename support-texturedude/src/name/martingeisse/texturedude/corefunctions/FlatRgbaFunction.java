/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude.corefunctions;

import java.io.IOException;

import name.martingeisse.texturedude.IFunction;
import name.martingeisse.texturedude.IFunctionHost;
import name.martingeisse.texturedude.Layer;
import name.martingeisse.texturedude.TextureDude;

/**
 * Arguments: R, G, B, A
 * Input layers: layer
 * Output values: layer
 *
 * Fills the whole layer with a uniform color and opacity.
 */
public class FlatRgbaFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final FlatRgbaFunction INSTANCE = new FlatRgbaFunction();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		call(host, true);
	}

	/**
	 * Common handling for RGB and RGBA cases.
	 * @param host the function host
	 * @param withAlpha whether an alpha argument is in the program
	 * @throws IOException on I/O errors
	 */
	public static void call(IFunctionHost host, boolean withAlpha) throws IOException {
		TextureDude dude = host.getDude();
		int r = host.fetchArgumentByte();
		int g = host.fetchArgumentByte();
		int b = host.fetchArgumentByte();
		int a = (withAlpha ? host.fetchArgumentByte() : 255);
		Layer layer = dude.getLayerStack().peekReference();
		byte[] pixels = layer.getPixels();
		for (int x=0; x<layer.getWidth(); x++) {
			for (int y=0; y<layer.getHeight(); y++) {
				int baseIndex = (y * layer.getWidth() + x) * 4;
				pixels[baseIndex + 0] = (byte)r;
				pixels[baseIndex + 1] = (byte)g;
				pixels[baseIndex + 2] = (byte)b;
				pixels[baseIndex + 3] = (byte)a;
			}
		}
	}

}
