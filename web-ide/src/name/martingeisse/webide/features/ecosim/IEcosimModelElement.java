/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim;

import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;

/**
 * Base interface for Eco32 simulation model elements.
 */
public interface IEcosimModelElement extends ISimulationModelElement {

	/**
	 * Returns the devices contributed by this model element.
	 * May return null instead of an empty array to indicate
	 * no contributed devices.
	 * 
	 * @return the devices contributed by this model element or null
	 */
	public EcosimContributedDevice[] getContributedDevices();
	
}
