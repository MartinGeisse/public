/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

import name.martingeisse.webide.ipc.IpcEvent;

/**
 * Wraps {@link IpcEvent}s from the simulator to avoid any component
 * subscribing to IPC events via Atmosphere receiving simulator
 * events.
 */
public final class SimulationEventMessage {

	/**
	 * the virtualMachine
	 */
	private final SimulatedVirtualMachine virtualMachine;
	
	/**
	 * the event
	 */
	private final IpcEvent event;

	/**
	 * Constructor.
	 * @param virtualMachine the virtual machine that produced the event
	 * @param event the event
	 */
	public SimulationEventMessage(SimulatedVirtualMachine virtualMachine, IpcEvent event) {
		this.virtualMachine = virtualMachine;
		this.event = event;
	}

	/**
	 * Getter method for the virtualMachine.
	 * @return the virtualMachine
	 */
	public SimulatedVirtualMachine getVirtualMachine() {
		return virtualMachine;
	}
	
	/**
	 * Getter method for the event.
	 * @return the event
	 */
	public IpcEvent getEvent() {
		return event;
	}
	
}
