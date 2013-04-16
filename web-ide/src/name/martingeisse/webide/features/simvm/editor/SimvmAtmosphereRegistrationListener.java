/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;

import org.apache.wicket.Page;
import org.apache.wicket.atmosphere.ResourceRegistrationListener;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Stores a mapping of Atmosphere UUIDs (each corresponding to a SimVM editor page)
 * to simulation anchor resource handles. This allows us to notify the correct
 * pages when a simulation asynchronously changes its UI.
 */
public final class SimvmAtmosphereRegistrationListener implements ResourceRegistrationListener {

	/* (non-Javadoc)
	 * @see org.apache.wicket.atmosphere.ResourceRegistrationListener#resourceRegistered(java.lang.String, org.apache.wicket.Page)
	 */
	@Override
	public void resourceRegistered(String uuid, Page page) {
		if (page instanceof IWorkbenchServicesProvider) {
			IWorkbenchServicesProvider servicesProvider = (IWorkbenchServicesProvider)page;
			Panel untypedEditorPanel = servicesProvider.getEditorService().getEditorPanel();
			if (untypedEditorPanel instanceof EditorPanel) {
				EditorPanel editorPanel = (EditorPanel)untypedEditorPanel;
				EditorPanel.editorPageSimulationAnchors.put(uuid, editorPanel.getAnchorResource());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.atmosphere.ResourceRegistrationListener#resourceUnregistered(java.lang.String)
	 */
	@Override
	public void resourceUnregistered(String uuid) {
		EditorPanel.editorPageSimulationAnchors.remove(uuid);
	}
	
}
