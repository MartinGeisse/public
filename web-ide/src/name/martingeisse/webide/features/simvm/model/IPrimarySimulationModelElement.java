/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

/**
 * Implemented by top-level (primary) model elements, i.e. model
 * elements that represent the whole simulated virtual machine.
 */
public interface IPrimarySimulationModelElement extends ISimulationModelElement {

	/**
	 * Performs a single simulation step, whatever that means for the
	 * concrete simulation model.
	 * 
	 * The intention of this method is to allow single-stepping the
	 * simulation. Depending on the model, it may be useful to make
	 * the meaning of a single step configurable. Implementations
	 * should assume that a distinct user action (key press, mouse
	 * click, etc.) is required for each single step.
	 */
	public void singleStep();

	/**
	 * Performs a batch simulation step, whatever that means for the
	 * concrete simulation model.
	 * 
	 * The intention of this method is to allow simulation regardless
	 * of user input. A batch step should behave like a series of
	 * single steps, but no assumption is made about the number of
	 * equivalent single steps. Implementations should perform
	 * just enough work in a batch step to ensure that the overhead
	 * of the outer event handling loop (which calls this method) is
	 * negligible.
	 */
	public void batchStep();

}
