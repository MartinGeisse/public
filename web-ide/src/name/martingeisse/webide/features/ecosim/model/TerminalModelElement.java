/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.model;

import name.martingeisse.ecosim.devices.terminal.TerminalController;
import name.martingeisse.webide.features.ecosim.ui.TerminalPanel;
import name.martingeisse.webide.features.simvm.model.AbstractSimulationModelElement;
import name.martingeisse.webide.features.simvm.model.ISimulationModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * The simulation model element for the terminals.
 */
public class TerminalModelElement extends AbstractSimulationModelElement implements IEcosimModelElement {

	/**
	 * the terminalController
	 */
	private TerminalController terminalController;
	
	/**
	 * the terminal
	 */
	private Terminal terminal;

	/**
	 * Constructor.
	 * @param eventOutbox the event outbox
	 */
	public TerminalModelElement(final IIpcEventOutbox eventOutbox) {
		this.terminalController = new TerminalController();
		this.terminal = new Terminal(eventOutbox);
		terminalController.setTerminal(terminal);
		terminal.setHost(terminalController);
	}

	/**
	 * Getter method for the terminalController.
	 * @return the terminalController
	 */
	public TerminalController getTerminalController() {
		return terminalController;
	}
	
	/**
	 * Getter method for the terminal.
	 * @return the terminal
	 */
	public Terminal getTerminal() {
		return terminal;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.AbstractSimulationModelElement#getDefaultTitle()
	 */
	@Override
	protected String getDefaultTitle() {
		return "Terminal";
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
		terminal.clearOutput();
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
		return new TerminalPanel(id, thisModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.ecosim.model.IEcosimModelElement#getContributedDevices()
	 */
	@Override
	public EcosimContributedDevice[] getContributedDevices() {
		return new EcosimContributedDevice[] {
			new EcosimContributedDevice(0x30300000, terminalController, new int[] {1, 0}),
		};
	}

	
}
