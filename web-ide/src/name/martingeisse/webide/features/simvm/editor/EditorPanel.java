/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import name.martingeisse.webide.document.Document;
import name.martingeisse.webide.features.simvm.model.PrimaryElementFromDocumentModel;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.atmosphere.AtmosphereBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * The actual Wicket component that encapsulates the SimVM UI.
 * 
 * This panel stores the page's Atmosphere UUID for push-updates and
 * renders the components of its simulation model elements.
 */
class EditorPanel extends Panel {

	/**
	 * This map maps Atmosphere resource UUIDs (each corresponding to a page with an
	 * EditorPanel) to {@link ResourceHandle} objects which correspond to running
	 * simulations. This allows to filter Atmosphere events without building a
	 * request cycle first.
	 * 
	 * The map may contain mappings for pages which don't show a simulation anymore
	 * (or which don't event exist anymore) without bad effects (other than consuming
	 * a small amount of memory); the only important invariant is that the mapping for
	 * existing pages which still show a running simulation must be correct and
	 * up-to-date.
	 */
	static final ConcurrentMap<String, ResourceHandle> editorPageSimulationAnchors = new ConcurrentHashMap<String, ResourceHandle>();
	
	/**
	 * Constructor.
	 * @param id the Wicket id
	 * @param model the document model
	 */
	public EditorPanel(final String id, final IModel<Document> model) {
		super(id, model);
		setOutputMarkupId(true);
		add(getSimulationModel().getPrimaryElement().createComponent("innerPanel", new PrimaryElementFromDocumentModel(model)));
	}

	/**
	 * @return the {@link IModel} for the {@link Document}
	 */
	@SuppressWarnings("unchecked")
	public final IModel<Document> getDocumentModel() {
		return (IModel<Document>)getDefaultModel();
	}
	
	/**
	 * @return the {@link Document}
	 */
	public final Document getDocument() {
		return getDocumentModel().getObject();
	}
	
	/**
	 * Getter method for the anchorResource.
	 * @return the anchorResource
	 */
	final ResourceHandle getAnchorResource() {
		return getDocument().getResourceHandle();
	}
	
	/**
	 * @return the {@link SimulatedVirtualMachine}
	 */
	final SimulatedVirtualMachine getVirtualMachine() {
		return (SimulatedVirtualMachine)getDocument().getBody();
	}
	
	/**
	 * @return the {@link SimulationModel}
	 */
	final SimulationModel getSimulationModel() {
		return getVirtualMachine().getSimulationModel();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onAfterRender()
	 */
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		
		// store the UUID to simulation mapping in case this panel gets rendered into a page
		// with a pre-existing connection
		String uuid = AtmosphereBehavior.getUUID(getPage());
		if (uuid != null) {
			editorPageSimulationAnchors.put(uuid, getAnchorResource());
		}
	}
	
}
