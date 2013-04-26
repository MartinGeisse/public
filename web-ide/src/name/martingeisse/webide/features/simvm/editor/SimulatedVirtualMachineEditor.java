/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.util.List;

import name.martingeisse.webide.editor.AbstractDocumentEditor;
import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;
import name.martingeisse.webide.resources.FetchMarkerResult;

import org.apache.wicket.Component;

/**
 * "Editor" implementation that shows the SimVM UI.
 * 
 * This editor uses the simulation model as its document since most parts
 * of the UI will want to display/manipulate that. Via the resource handle,
 * it is also possible to obtain the running simulation and control it.
 */
public class SimulatedVirtualMachineEditor extends AbstractDocumentEditor {

	/**
	 * Constructor.
	 */
	public SimulatedVirtualMachineEditor() {
		super(SimulatedVirtualMachine.class);
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
