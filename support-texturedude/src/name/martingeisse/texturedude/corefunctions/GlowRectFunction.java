/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude.corefunctions;

import java.io.IOException;

import name.martingeisse.texturedude.IFunction;
import name.martingeisse.texturedude.IFunctionHost;

/**
 * Arguments:
 * 		centerX, centerY,
 * 		rectWidth, rectHeight,
 * 		glowRadius
 * 		color (R, G, B),
 * Input layers: layer
 * Output values: layer
 *
 * Drawsa rectangle with a rounded halo.
 */
public final class GlowRectFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final GlowRectFunction INSTANCE = new GlowRectFunction();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		/*
		TextureDude dude = host.getDude();
		Layer layer = dude.getLayerStack().peekReference();
		byte[] pixels = layer.getPixels();
		
		int centerX = host.fetchArgumentByte(), centerY = host.fetchArgumentByte();
		int rectWidth = host.fetchArgumentByte(), rectHeight = host.fetchArgumentByte();
		int glowRadius = host.fetchArgumentByte();
		int r = host.fetchArgumentByte(), g = host.fetchArgumentByte(), b = host.fetchArgumentByte();
		*/
	}
	
}
