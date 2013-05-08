/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.debugout;

import name.martingeisse.webide.features.ecosim.EcosimContributedDevice;
import name.martingeisse.webide.features.ecosim.IEcosimModelElement;
import name.martingeisse.webide.features.simvm.model.AbstractSimulationModelElement;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * The simulation model element for the debug output log.
 */
public class DebugOutputModelElement extends AbstractSimulationModelElement implements IEcosimModelElement {

	/**
	 * the controller
	 */
	private DebugOutputController controller;

	/**
	 * Constructor.
	 * @param eventOutbox the event outbox
	 */
	public DebugOutputModelElement(final IIpcEventOutbox eventOutbox) {
		this.controller = new DebugOutputController(eventOutbox);
	}

	/**
	 * Getter method for the controller.
	 * @return the controller
	 */
	public DebugOutputController getController() {
		return controller;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.AbstractSimulationModelElement#getDefaultTitle()
	 */
	@Override
	protected String getDefaultTitle() {
		return "Debug";
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#initialize(name.martingeisse.webide.features.simvm.model.SimulationModel, name.martingeisse.webide.ipc.IIpcEventOutbox)
	 */
	@Override
	public void initialize(SimulationModel simulationModel, IIpcEventOutbox eventOutbox) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#saveRuntimeState()
	 */
	@Override
	public Object saveRuntimeState() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#loadRuntimeState(java.lang.Object)
	 */
	@Override
	public void loadRuntimeState(Object state) {
		controller.clearOutput();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#deleteSavedRuntimeState()
	 */
	@Override
	public void deleteSavedRuntimeState() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#handleEvent(name.martingeisse.webide.ipc.IpcEvent)
	 */
	@Override
	public void handleEvent(IpcEvent event) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.ISimulationModelElement#createComponent(java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public Component createComponent(String id, IModel<ISimulationModelElement> thisModel) {
		return new DebugOutputPanel(id, thisModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.ecosim.model.IEcosimModelElement#getContributedDevices()
	 */
	@Override
	public EcosimContributedDevice[] getContributedDevices() {
		return new EcosimContributedDevice[] {
			new EcosimContributedDevice(0x3F000000, controller, new int[] {}),
		};
	}

	
}
