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
 * Input layers: layer
 * Output values: layer
 *
 * Copies the top-of-stack layer into a slot.
 */
public class CopyToSlotFunction implements IFunction {

	/**
	 * The shared instance of this class.
	 */
	public static final CopyToSlotFunction INSTANCE = new CopyToSlotFunction();

	/* (non-Javadoc)
	 * @see name.martingeisse.texturedude.IFunction#call(name.martingeisse.texturedude.IFunctionHost)
	 */
	@Override
	public void call(IFunctionHost host) throws IOException {
		TextureDude dude = host.getDude();
		dude.getLayerSlots().set(host.fetchArgumentByte(), dude.getLayerStack().peekCopy());
	}
	
}
