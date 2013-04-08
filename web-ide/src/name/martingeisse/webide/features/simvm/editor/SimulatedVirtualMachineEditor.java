/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.model.PropertyModel;

import name.martingeisse.webide.editor.AbstractEditor;
import name.martingeisse.webide.resources.FetchMarkerResult;

/**
 * "Editor" implementation that shows the SimVM UI.
 */
public class SimulatedVirtualMachineEditor extends AbstractEditor<String> {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.AbstractEditor#createDocument(byte[])
	 */
	@Override
	protected String createDocument(byte[] resourceData) {
		return new String(resourceData, Charset.forName("utf-8"));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(String id) {
		return new EditorPanel(id, new PropertyModel<String>(this, "document"));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#updateMarkers(java.util.List)
	 */
	@Override
	public void updateMarkers(List<FetchMarkerResult> markers) {
	}
	
}
