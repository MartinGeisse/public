/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.util.List;

import name.martingeisse.webide.editor.AbstractEditor;
import name.martingeisse.webide.features.simvm.model.IPrimarySimulatorModelElement;
import name.martingeisse.webide.features.simvm.model.SimulatorModel;
import name.martingeisse.webide.features.simvm.model.StepwisePrimarySimulatorModelElement;
import name.martingeisse.webide.resources.FetchMarkerResult;

import org.apache.wicket.Component;

/**
 * "Editor" implementation that shows the SimVM UI.
 */
public class SimulatedVirtualMachineEditor extends AbstractEditor<SimulatorModel> {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.AbstractEditor#createDocument(byte[])
	 */
	@Override
	protected SimulatorModel createDocument(byte[] resourceData) {
		IPrimarySimulatorModelElement primaryElement = new StepwisePrimarySimulatorModelElement();
		return new SimulatorModel(primaryElement, getWorkspaceResourceHandle());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(String id) {
		return new EditorPanel(id, createDocumentModel());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#updateMarkers(java.util.List)
	 */
	@Override
	public void updateMarkers(List<FetchMarkerResult> markers) {
	}
	
}
