/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim.console;

import name.martingeisse.ecosim.devices.chardisplay.CharacterDisplayController;
import name.martingeisse.ecosim.devices.keyboard.KeyboardController;
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
 * The simulation model element for the console (keyboard
 * and character display).
 */
public class ConsoleModelElement extends AbstractSimulationModelElement implements IEcosimModelElement {

	/**
	 * the keyboardController
	 */
	private KeyboardController keyboardController;
	
	/**
	 * the displayController
	 */
	private CharacterDisplayController displayController;
	
	/**
	 * the console
	 */
	private Console console;
	
	/**
	 * Constructor.
	 * @param eventOutbox the event outbox
	 */
	public ConsoleModelElement(final IIpcEventOutbox eventOutbox) {
		this.keyboardController = new KeyboardController();
		this.displayController = new CharacterDisplayController();
		this.console = new Console(eventOutbox, keyboardController, displayController);
		keyboardController.setKeyboard(console);
		displayController.setDisplay(console);
		
		// TODO: remove
		displayController.initDemoValues();
	}
	
	/**
	 * Getter method for the keyboardController.
	 * @return the keyboardController
	 */
	public KeyboardController getKeyboardController() {
		return keyboardController;
	}
	
	/**
	 * Getter method for the displayController.
	 * @return the displayController
	 */
	public CharacterDisplayController getDisplayController() {
		return displayController;
	}
	
	/**
	 * Getter method for the console.
	 * @return the console
	 */
	public Console getConsole() {
		return console;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.AbstractSimulationModelElement#getDefaultTitle()
	 */
	@Override
	protected String getDefaultTitle() {
		return "Console";
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
		displayController.clear();
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
		return new ConsolePanel(id, thisModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.ecosim.IEcosimModelElement#getContributedDevices()
	 */
	@Override
	public EcosimContributedDevice[] getContributedDevices() {
		return new EcosimContributedDevice[] {
			new EcosimContributedDevice(0x30100000, displayController, new int[] {}),
			new EcosimContributedDevice(0x30200000, keyboardController, new int[] {4}),
		};
	}
	
}
