/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim;

import name.martingeisse.webide.features.simvm.model.AbstractTwoLevelCompositeSimulationModelElement;

/**
 * The primary model element for the Eco32 simulator.
 */
public class EcosimPrimaryModelElement extends AbstractTwoLevelCompositeSimulationModelElement<IEcosimModelElement>{

	/**
	 * the INSTRUCTIONS_PER_DEVICE_TICK
	 */
	public static final int INSTRUCTIONS_PER_DEVICE_TICK = 500;
	
	/**
	 * Constructor.
	 */
	public EcosimPrimaryModelElement() {
		super(INSTRUCTIONS_PER_DEVICE_TICK);
	}
	
}
