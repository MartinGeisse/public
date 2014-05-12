/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude.corefunctions;

import java.io.IOException;

import name.martingeisse.texturedude.IFunction;
import name.martingeisse.texturedude.IFunctionHost;
import name.martingeisse.texturedude.TextureDude;

/**
 * Arguments: slotIndex
 * Input layers: -
 * Output values: layer
 *
 * Copies a layer from a slot onto the stack.
 */
public class CopyFromSlotFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final CopyFromSlotFunction INSTANCE = new CopyFromSlotFunction();

	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		TextureDude dude = host.getDude();
		dude.getLayerStack().pushCopy(dude.getLayerSlots().get(host.fetchArgumentByte()));
	}
	
}
