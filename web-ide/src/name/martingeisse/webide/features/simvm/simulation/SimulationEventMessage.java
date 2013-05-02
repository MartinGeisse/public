/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

import org.atmosphere.cpr.AtmosphereResource;

import name.martingeisse.webide.features.simvm.editor.AtmosphereResourceCaptureFilter.ISetter;
import name.martingeisse.webide.ipc.IpcEvent;

/**
 * Wraps {@link IpcEvent}s from the simulator to avoid any component
 * subscribing to IPC events via Atmosphere receiving simulator
 * events.
 */
public final class SimulationEventMessage implements ISetter {

	/**
	 * the virtualMachine
	 */
	private final SimulatedVirtualMachine virtualMachine;
	
	/**
	 * the event
	 */
	private final IpcEvent event;
	
	/**
	 * the currentResource
	 */
	private AtmosphereResource currentResource;

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

	/**
	 * Getter method for the currentResource.
	 * @return the currentResource
	 */
	public AtmosphereResource getCurrentResource() {
		return currentResource;
	}
	
	/**
	 * Setter method for the currentResource.
	 * @param currentResource the currentResource to set
	 */
	public void setCurrentResource(AtmosphereResource currentResource) {
		this.currentResource = currentResource;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.editor.AtmosphereResourceCaptureFilter.ISetter#set(org.atmosphere.cpr.AtmosphereResource)
	 */
	@Override
	public void set(AtmosphereResource resource) {
		setCurrentResource(resource);
	}
	
}
