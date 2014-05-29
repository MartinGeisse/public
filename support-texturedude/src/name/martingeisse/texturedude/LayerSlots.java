/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude;

/**
 * Maintains the layer slots of the VM. These can be used as temporary
 * storage for partial generation results that must be moved off the
 * stack, then brought back later.
 * 
 * There are 256 Slots. This number is fixed so that drawing code knows
 * on how many slots it can rely, and also to permit a straightforward
 * instruction set.
 */
public final class LayerSlots {

	/**
	 * the layers
	 */
	private final Layer[] layers;
	
	/**
	 * Constructor.
	 */
	public LayerSlots() {
		this.layers = new Layer[256];
	}
	
	/**
	 * Sets a layer entry.
	 * @param index the index of the entry
	 * @param layer the layer to store in the entry
	 */
	public void set(int index, Layer layer) {
		layers[index] = layer;
	}

	/**
	 * Gets a layer entry.
	 * @param index the index of the entry
	 * @return the layer from that entry
	 */
	public Layer get(int index) {
		return layers[index];
	}

	/**
	 * Gets a layer entry and sets it to null at the same time.
	 * @param index the index of the entry
	 * @return the layer that was in that entry
	 */
	public Layer remove(int index) {
		Layer result = layers[index];
		layers[index] = null;
		return result;
	}

}
