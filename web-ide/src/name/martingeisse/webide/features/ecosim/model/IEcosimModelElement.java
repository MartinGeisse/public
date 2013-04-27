/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.model;

import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;

/**
 * Base interface for Eco32 simulation model elements.
 * 
 * TODO: better terminology! The word "model" is totally overloaded.
 * SimulationModel -> VirtualMachineInterior (?)
 * SimulationModelElement -> VirtualMachineElement
 * 	 etc.
 * Terminal -> TerminalController
 * TerminalUserInterface -> Terminal
 * TerminalPanel kann bleiben
 * 
 * Es kann N TerminalController zur VM geben (z.B. 2 für Eco32),
 * 1 Terminal je Controller, N Panels je Terminal (entspricht N
 * Benutzern, die auf dasselbe Terminal sehen)
 * 
 * -> damit entsprechen die Begriffe auf der VM-Welt wieder denen
 * aus der realen Hardware
 * 
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
