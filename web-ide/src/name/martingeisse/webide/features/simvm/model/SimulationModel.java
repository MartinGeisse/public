/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.io.Serializable;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.util.ParameterUtil;
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
	 */
	public SimulationModel(ResourceHandle descriptorResource) {
		try {
			JsonAnalyzer simulationModelAnalyzer = JsonAnalyzer.parse(descriptorResource.readTextFile(true));
			String primaryElementClassName = simulationModelAnalyzer.analyzeMapElement("primaryElementClass").expectString();
			Class<?> primaryElementClass = getClass().getClassLoader().loadClass(primaryElementClassName);
			this.primaryElement = (IPrimarySimulationModelElement)primaryElementClass.newInstance();
			this.anchorResource = descriptorResource;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		initializePrimaryElement();
	}

	/**
	 * Constructor.
	 * @param primaryElement the primary model element
	 * @param anchorResource the resource used as an anchor for loading auxiliary files
	 */
	public SimulationModel(IPrimarySimulationModelElement primaryElement, ResourceHandle anchorResource) {
		this.primaryElement = ParameterUtil.ensureNotNull(primaryElement, "primaryElement");
		this.anchorResource = ParameterUtil.ensureNotNull(anchorResource, "anchorResource");;
		initializePrimaryElement();
	}

	/**
	 * Initializes the primary model element with this object.
	 */
	private void initializePrimaryElement() {
		primaryElement.initialize(this);
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
