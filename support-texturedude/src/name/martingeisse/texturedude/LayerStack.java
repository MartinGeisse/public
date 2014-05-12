/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude;


/**
 * Maintains the layer stack of the VM. The stack is used for generation
 * instructions to load input layers and store output layers. The
 * stack maintains a strict FIFO order.
 * 
 * The maximum stack size is currently fixed to 256. This number is fixed
 * so that drawing code knows on how many stack entries it can rely. The
 * instruction set is not affected by that size due to the strict FIFO
 * order.
 */
public final class LayerStack {

	/**
	 * the layers
	 */
	private final Layer[] layers;
	
	/**
	 * the size
	 */
	private int size;
	
	/**
	 * Constructor.
	 */
	public LayerStack() {
		this.layers = new Layer[256];
		this.size = 0;
	}

	/**
	 * Pushes a reference to the specified layer on the stack.
	 * 
	 * @param layer the layer reference to push
	 */
	public void pushReference(Layer layer) {
		if (size == layers.length) {
			throw new IllegalStateException("stack overflow");
		}
		layers[size] = layer;
		size++;
	}
	
	/**
	 * Pushes a copy of the specified layer on the stack.
	 * 
	 * @param layer the layer to copy and push
	 */
	public void pushCopy(Layer layer) {
		pushReference(layer.clone());
	}
	
	/**
	 * Pops a layer off the stack and returns it.
	 * 
	 * @return the layer
	 */
	public Layer pop() {
		if (size == 0) {
			throw new IllegalStateException("stack underflow");
		}
		size--;
		return layers[size];
	}
	
	/**
	 * Takes the top-of-stack layer and returns a reference to it,
	 * but leaves it on the stack.
	 * 
	 * @return the top-of-stack layer
	 */
	public Layer peekReference() {
		if (size == 0) {
			throw new IllegalStateException("stack underflow");
		}
		return layers[size - 1];
	}
	
	/**
	 * Makes a copy of the top-of-stack layer and returns it,
	 * but leaves the original on the stack.
	 * 
	 * @return the copy of the top-of-stack layer
	 */
	public Layer peekCopy() {
		return peekReference().clone();
	}
	
}
