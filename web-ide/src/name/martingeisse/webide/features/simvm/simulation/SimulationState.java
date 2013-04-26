/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

/**
 * Represents the overall states of a {@link SimulatedVirtualMachine} object.
 */
public enum SimulationState {

	/**
	 * The simulation is currently running.
	 */
	RUNNING,
	
	/**
	 * The simulation is paused, but the simulation thread still exists.
	 */
	PAUSED,
	
	/**
	 * The simulation has been suspended or terminated and the simulation
	 * thread has been stopped.
	 */
	STOPPED
	
}
