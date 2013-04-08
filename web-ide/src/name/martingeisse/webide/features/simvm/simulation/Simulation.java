/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import name.martingeisse.webide.features.simvm.model.SimulatorModel;
import name.martingeisse.webide.resources.ResourceHandle;

/**
 * Represents a running simulation. Each instance builds its own
 * {@link SimulatorModel} and keeps associated state.
 * 
 * When the simulation is suspended, the {@link Simulation} instance
 * writes runtime state to the workspace and becomes stale. It should
 * not be used after that. Note that this rule does not include "active
 * pause" states, e.g. breakpoints or single-stepping; in the latter
 * case, the {@link Simulation} instance remains intact and can actually
 * be used to control the active-pause state.
 * 
 * Race conditions may lead to this object becoming stale just before
 * invoking a method. Callers should handle the corresponding exception
 * gracefully and avoid showing a user-visible error.
 * 
 * All methods are implemented as events posted to the simulation
 * thread. This implies that immediate feedback is not available. Callers
 * should also be prepared for crashed simulations that cannot respond
 * to such events properly.
 * 
 * Neither terminating nor suspending the simulation deletes its persistent
 * state (e.g. simulated disk contents).
 */
public final class Simulation {

	/**
	 * the simulations
	 */
	private static final ConcurrentMap<ResourceHandle, Simulation> simulations = new ConcurrentHashMap<ResourceHandle, Simulation>();
	
	/**
	 * Returns the instance for the specified resource, or null if none exists.
	 *  
	 * @param resourceHandle the resource that represents the simulator model
	 * @return the {@link Simulation} instance or null
	 */
	public static Simulation getExisting(ResourceHandle resourceHandle) {
		return simulations.get(resourceHandle);
	}
	
	/**
	 * Returns the instance for the specified resource, creating it if
	 * it doesn't exist yet.
	 *  
	 * @param resourceHandle the resource that represents the simulator model
	 * @param resumeOnCreate if the simulation does not yet exist, this flag controls whether
	 * the simulation is immediately resumed or left in "paused" state.
	 * @return the {@link Simulation} instance
	 */
	public static Simulation getOrCreate(ResourceHandle resourceHandle, boolean resumeOnCreate) {
		Simulation newSimulation = new Simulation(resourceHandle);
		Simulation existing = simulations.putIfAbsent(resourceHandle, newSimulation);
		if (existing != null) {
			return existing;
		}
		// TODO initialize
		// TODO not thread-safe; other threads might use the simulation right now
		if (resumeOnCreate) {
			newSimulation.resume();
		}
		return newSimulation;
	}
	
	/**
	 * the resourceHandle
	 */
	private final ResourceHandle resourceHandle;
	
	/**
	 * the running
	 */
	private boolean running;
	
	/**
	 * the stale
	 */
	private boolean stale;
	
	/**
	 * Constructor.
	 */
	private Simulation(ResourceHandle resourceHandle) {
		this.resourceHandle = resourceHandle;
		this.running = false;
		this.stale = false;
	}

	/**
	 * Getter method for the resourceHandle.
	 * @return the resourceHandle
	 */
	public ResourceHandle getResourceHandle() {
		return resourceHandle;
	}
	
	/**
	 * Getter method for the running.
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Getter method for the stale.
	 * @return the stale
	 */
	public boolean isStale() {
		return stale;
	}
	
	/**
	 * Pauses this simulation. The simulation stops simulating and waits for
	 * further method calls. Has no effect if the simulation is already paused.
	 * 
	 * @throws StaleSimulationException if this object is stale
	 */
	public synchronized void pause() throws StaleSimulationException {
		checkNotStale();
		// TODO: post event
	}
	
	/**
	 * Performs a single simulation step. The exact meaning depends on the
	 * simulation model. Has no effect if the simulation is currently running.
	 * 
	 * @throws StaleSimulationException if this object is stale
	 */
	public synchronized void step() throws StaleSimulationException {
		checkNotStale();
		// TODO: post event
	}
	
	/**
	 * Resumes simulation from "paused" state.
	 * Has no effect if the simulation is currently running.
	 * 
	 * @throws StaleSimulationException if this object is stale
	 */
	public synchronized void resume() throws StaleSimulationException {
		checkNotStale();
		// TODO: post event
	}

	/**
	 * Suspends this simulation, writing its runtime state to disk. This object
	 * becomes stale by using this method and should not be used anymore.
	 * 
	 * @throws StaleSimulationException if this object is stale
	 */
	public synchronized void suspend() throws StaleSimulationException {
		checkNotStale();
		// TODO: post event
		
		// TODO: remove this line, used for fake implementation
		simulations.remove(resourceHandle);
	}
	
	/**
	 * Terminates this simulation, deleting all runtime state. This object
	 * becomes stale by using this method and should not be used anymore.
	 * 
	 * @throws StaleSimulationException if this object is stale
	 */
	public synchronized void terminate() throws StaleSimulationException {
		checkNotStale();
		// TODO: post event
		
		// TODO: remove this line, used for fake implementation
		simulations.remove(resourceHandle);
	}
	
	/**
	 * Throws a {@link StaleSimulationException} if this object is stale.
	 */
	private void checkNotStale() {
		if (stale) {
			throw new StaleSimulationException();
		}
	}
	
	/**
	 * This exception type gets thrown when a stale {@link Simulation}
	 * object is used. This can easily occur due to race conditions and
	 * should not normally cause a user-visible error message; instead,
	 * the caller should obtain the up-to-date {@link Simulation} object
	 * (if any) again and re-try the operation.
	 */
	public static class StaleSimulationException extends IllegalStateException {
		
		/**
		 * Constructor.
		 */
		StaleSimulationException() {
			super("this simulation object is stale");
		}
		
	}
	
}
