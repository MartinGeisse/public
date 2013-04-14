/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.io.Serializable;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.resources.ResourceHandle;

/**
 * A high-level model of the simulation, including both its
 * non-changing definition and its volatile runtime state.
 * The simulation model is first built from a SimVM definition
 * file, then asked to load runtime state.
 */
public final class SimulationModel implements Serializable {

	/**
	 * the primaryElement
	 */
	private final IPrimarySimulationModelElement primaryElement;
	
	/**
	 * the anchorResource
	 */
	private final ResourceHandle anchorResource;

	/**
	 * Constructor.
	 * @param descriptorResource the resource that contains the descriptor for the model
	 * @param eventOutbox the event outbox
	 */
	public SimulationModel(ResourceHandle descriptorResource, IIpcEventOutbox eventOutbox) {
		try {
			JsonAnalyzer simulationModelAnalyzer = JsonAnalyzer.parse(descriptorResource.readTextFile(true));
			String primaryElementClassName = simulationModelAnalyzer.analyzeMapElement("primaryElementClass").expectString();
			Class<?> primaryElementClass = getClass().getClassLoader().loadClass(primaryElementClassName);
			this.primaryElement = (IPrimarySimulationModelElement)primaryElementClass.newInstance();
			this.anchorResource = descriptorResource;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		primaryElement.initialize(this, eventOutbox);
	}
	
	/**
	 * Getter method for the primaryElement.
	 * @return the primaryElement
	 */
	public IPrimarySimulationModelElement getPrimaryElement() {
		return primaryElement;
	}

	/**
	 * Getter method for the anchorResource.
	 * @return the anchorResource
	 */
	public ResourceHandle getAnchorResource() {
		return anchorResource;
	}
	
}
