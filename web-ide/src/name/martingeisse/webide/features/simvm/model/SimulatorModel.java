/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import java.io.Serializable;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.webide.resources.ResourceHandle;

/**
 * A high-level model of the simulator (not including its
 * runtime state). The simulator model is built from a
 * SimVM definition file.
 */
public final class SimulatorModel implements Serializable {

	/**
	 * the primaryElement
	 */
	private final IPrimarySimulatorModelElement primaryElement;
	
	/**
	 * the anchorResource
	 */
	private final ResourceHandle anchorResource;
	
	/**
	 * Constructor.
	 * @param primaryElement the primary model element
	 * @param anchorResource the resource used as an anchor for loading auxiliary files
	 */
	public SimulatorModel(IPrimarySimulatorModelElement primaryElement, ResourceHandle anchorResource) {
		this.primaryElement = ParameterUtil.ensureNotNull(primaryElement, "primaryElement");
		this.anchorResource = ParameterUtil.ensureNotNull(anchorResource, "anchorResource");;
	}

	/**
	 * Getter method for the primaryElement.
	 * @return the primaryElement
	 */
	public IPrimarySimulatorModelElement getPrimaryElement() {
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
