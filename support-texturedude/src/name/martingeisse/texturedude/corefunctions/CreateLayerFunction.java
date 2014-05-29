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

/**
 * Arguments: encodedWidth, encodedHeight
 * Input layers: -
 * Output values: newLayer
 *
 * Creates a new layer with the specified size and pushes it on
 * the layer stack.
 * 
 * For both width and height, if 0 is specified, it gets interpreted
 * as 256. This is because size 0 makes no sense, but power-of-two
 * sizes are often used for textures.
 */
public class CreateLayerFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final CreateLayerFunction INSTANCE = new CreateLayerFunction();

	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		Layer layer = new Layer(fetchSizeArgument(host), fetchSizeArgument(host));
		host.getDude().getLayerStack().pushReference(layer);
	}

	/**
	 * Fetches and decodes width or height.
	 */
	private int fetchSizeArgument(IFunctionHost host) throws IOException {
		int value = (host.fetchArgumentByte() & 0xff);
		return (value == 0 ? 256 : value);
	}
	
}
