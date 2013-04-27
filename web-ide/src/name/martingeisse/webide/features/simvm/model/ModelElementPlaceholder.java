/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

import name.martingeisse.webide.ipc.IIpcEventOutbox;
import name.martingeisse.webide.ipc.IpcEvent;

/**
 * This class can be used to represent a simulation model
 * element that has not been written yet. It makes the
 * future element show up in the UI.
 */
public class ModelElementPlaceholder extends AbstractSimulationModelElement {

	/**
	 * the bodyText
	 */
	private String bodyText;
	
	/**
	 * Constructor.
	 * @param title the title for the UI
	 * @param bodyText the body text for the UI
	 */
	public ModelElementPlaceholder(String title, String bodyText) {
		setTitle(title);
		this.bodyText = bodyText;
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
		return new Label(id, bodyText);
	}
	
}
