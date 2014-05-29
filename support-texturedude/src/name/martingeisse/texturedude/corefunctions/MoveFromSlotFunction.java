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
 * Moves a layer out of a slot and onto the stack.
 */
public class MoveFromSlotFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final MoveFromSlotFunction INSTANCE = new MoveFromSlotFunction();

	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		TextureDude dude = host.getDude();
		dude.getLayerStack().pushReference(dude.getLayerSlots().remove(host.fetchArgumentByte()));
	}
	
}
