/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.io.Serializable;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;
import name.martingeisse.webide.ipc.IIpcEventOutbox;

/**
 * A high-level model of the simulation, including both its
 * non-changing definition and its volatile runtime state.
 * The simulation model is first built from a SimVM definition
 * file, then asked to load runtime state.
 */
public final class SimulationModel implements Serializable {
	
	/**
	 * the virtualMachine
	 */
	private SimulatedVirtualMachine virtualMachine;

	/**
	 * the primaryElement
	 */
	private final IPrimarySimulationModelElement primaryElement;
	
	/**
	 * Constructor.
	 * @param virtualMachine the virtual machine that uses this model
	 * @param eventOutbox the event outbox
	 */
	public SimulationModel(SimulatedVirtualMachine virtualMachine, IIpcEventOutbox eventOutbox) {
		this.virtualMachine = virtualMachine;
		try {
			JsonAnalyzer simulationModelAnalyzer = JsonAnalyzer.parse(virtualMachine.getDocument().getResourceHandle().readTextFile(true));
			String primaryElementClassName = simulationModelAnalyzer.analyzeMapElement("primaryElementClass").expectString();
			Class<?> primaryElementClass = getClass().getClassLoader().loadClass(primaryElementClassName);
			this.primaryElement = (IPrimarySimulationModelElement)primaryElementClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		primaryElement.initialize(this, eventOutbox);
	}
	
	/**
	 * Getter method for the virtualMachine.
	 * @return the virtualMachine
	 */
	public SimulatedVirtualMachine getVirtualMachine() {
		return virtualMachine;
	}
	
	/**
	 * Getter method for the primaryElement.
	 * @return the primaryElement
	 */
	public IPrimarySimulationModelElement getPrimaryElement() {
		return primaryElement;
	}
	
}
