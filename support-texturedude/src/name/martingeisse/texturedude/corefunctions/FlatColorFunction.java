/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude.corefunctions;

import java.io.IOException;

import name.martingeisse.texturedude.IFunction;
import name.martingeisse.texturedude.IFunctionHost;

/**
 * Arguments: R, G, B
 * Input layers: layer
 * Output values: layer
 *
 * Fills the whole layer with a uniform color and full opacity.
 */
public class FlatColorFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final FlatColorFunction INSTANCE = new FlatColorFunction();

	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		FlatRgbaFunction.call(host, false);
	}
	
}
