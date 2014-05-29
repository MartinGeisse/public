/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

/**
 * Defines names for the simulation events.
 */
public final class SimulationEvents {

	/**
	 * The start event. This event is published by a simulation when
	 * it has started, whether in running or paused mode.
	 */
	public static final String EVENT_TYPE_START = "simvm.start";

	/**
	 * The pause event. Can be sent to a running simulation to put
	 * it into pause mode. The simulation will in turn publish
	 * this event type to its listeners after entering pause mode.
	 */
	public static final String EVENT_TYPE_PAUSE = "simvm.pause";
	
	/**
	 * The step event. Can be sent to a paused simulation to
	 * execute a single step. The simulation will in turn publish
	 * this event type to its listeners after performing the step.
	 */
	public static final String EVENT_TYPE_STEP = "simvm.step";
	
	/**
	 * The resume event. Can be sent to a paused simulation to resume
	 * it. The simulation will in turn publish this event type to its
	 * listeners after resuming.
	 */
	public static final String EVENT_TYPE_RESUME = "simvm.resume";
	
	/**
	 * The suspend event. Can be sent to a running or paused
	 * simulation to save its state, then stop the simulation.
	 * The simulation will in turn publish this event type to its
	 * listeners just before it finally exits.
	 */
	public static final String EVENT_TYPE_SUSPEND = "simvm.suspend";
	
	/**
	 * The terminate event. Can be sent to a running or paused
	 * simulation to delete all saved state, then stop the simulation.
	 * The simulation will in turn publish this event type to its
	 * listeners just before it finally exits.
	 */
	public static final String EVENT_TYPE_TERMINATE = "simvm.terminate";

	/**
	 * Prevent instantiation.
	 */
	private SimulationEvents() {
	}
	
}
