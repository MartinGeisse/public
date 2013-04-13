/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.io.Serializable;
import java.util.List;

import name.martingeisse.webide.editor.IEditor;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.features.simvm.simulation.Simulation;
import name.martingeisse.webide.resources.FetchMarkerResult;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.Component;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * "Editor" implementation that shows the SimVM UI.
 * 
 * This editor uses the simulation model as its document since most parts
 * of the UI will want to display/manipulate that. Via the resource handle,
 * it is also possible to obtain the running simulation and control it.
 */
public class SimulatedVirtualMachineEditor implements IEditor, Serializable {

	/**
	 * the resourceHandle
	 */
	private ResourceHandle resourceHandle;
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#initialize(name.martingeisse.webide.resources.ResourceHandle)
	 */
	@Override
	public void initialize(ResourceHandle resourceHandle) {
		this.resourceHandle = resourceHandle;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(String id) {
		return new EditorPanel(id, new SimulationModelModel(), resourceHandle);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#getWorkspaceResourceHandle()
	 */
	@Override
	public ResourceHandle getWorkspaceResourceHandle() {
		return resourceHandle;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#getDocument()
	 */
	@Override
	public SimulationModel getDocument() {
		if (resourceHandle == null) {
			return null;
		}
		Simulation simulation = Simulation.getExisting(resourceHandle);
		if (simulation == null) {
			return null;
		}
		return simulation.getSimulationModel();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#updateMarkers(java.util.List)
	 */
	@Override
	public void updateMarkers(List<FetchMarkerResult> markers) {
	}

	/**
	 * This Wicket model returns the simulation model.
	 */
	private class SimulationModelModel extends AbstractReadOnlyModel<SimulationModel> {
		@Override
		public SimulationModel getObject() {
			return getDocument();
		}
	}
	
}
