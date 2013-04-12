/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.io.Serializable;

import name.martingeisse.webide.ipc.IpcEvent;

/**
 * Base interface for all types of model elements in a simulation.
 * 
 * Saving persistent state of simulation models is done via JSON and
 * auxiliary workspace resources. This interface extends
 * {@link Serializable} only to be compatible with the Wicket page store.
 */
public interface ISimulationModelElement extends Serializable {
	
	/**
	 * Informs this primary element about the top-level simulation
	 * model that wraps it.
	 * 
	 * @param simulationModel the enclosing simulation model
	 */
	public void initialize(SimulationModel simulationModel);
	
	/**
	 * Saves the state to a JSON structure and optionally to auxiliary workspace
	 * resources. The JSON structure is returned. Auxiliary resources should be
	 * used if the state includes parts that are too cumbersome for JSON (such as
	 * a RAM dump).
	 * 
	 * The location of auxiliary resources depends primarily on the anchor resource of
	 * the simulation model that was passed to {@link #initialize(SimulationModel)}.
	 * 
	 * @return the JSON structure
	 */
	public Object saveState();

	/**
	 * Loads the state from a JSON structure and optionally from auxiliary workspace
	 * resources. This method is the counterpart to {@link #saveState()}.
	 * 
	 * @param state the JSON structure
	 */
	public void loadState(Object state);

	/**
	 * Deletes the state stored in auxiliary workspace resources.
	 */
	public void deleteState();

	/**
	 * Handles an event that was passed to the simulation. This is
	 * mainly used to pass user input from the simulator UI to
	 * the simulation model.
	 * 
	 * @param event the event to handle
	 */
	public void handleEvent(IpcEvent<?> event);
	
}
