/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

/**
 * Represents the overall states of a {@link Simulation} object.
 */
public enum SimulationState {

	/**
	 * The simulation is currently running.
	 */
	RUNNING,
	
	/**
	 * The simulation is paused, but the {@link Simulation} object
	 * is still valid and could be resumed.
	 */
	PAUSED,
	
	/**
	 * The simulation has been suspended or terminated and the
	 * {@link Simulation} object has become stale. This does not
	 * preclude another {@link Simulation} object for the same
	 * simulation to be active.
	 */
	STOPPED
	
}
