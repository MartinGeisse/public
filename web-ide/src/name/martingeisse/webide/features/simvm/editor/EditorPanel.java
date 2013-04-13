/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import name.martingeisse.webide.features.ecosim.EcosimPrimaryModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.features.simvm.simulation.Simulation;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * The actual Wicket component that encapsulates the SimVM UI.
 */
class EditorPanel extends Panel {

	/**
	 * the anchorResource
	 */
	private final ResourceHandle anchorResource;
	
	/**
	 * Constructor.
	 * @param id the Wicket id
	 * @param model the document model
	 * @param anchorResource the anchor resource of the simulation
	 */
	public EditorPanel(String id, IModel<SimulationModel> model, ResourceHandle anchorResource) {
		super(id, model);
		this.anchorResource = anchorResource;
		
		add(new Link<Void>("startButton") {
			
			@Override
			public void onClick() {
				try {
					Simulation.getOrCreate(EditorPanel.this.anchorResource, true);
				} catch (Simulation.StaleSimulationException e) {
				}
			}
			
			@Override
			public boolean isVisible() {
				return !isRunning();
			}
			
		});
		
		add(new Link<Void>("terminateButton") {
			
			@Override
			public void onClick() {
				Simulation simulation = Simulation.getExisting(EditorPanel.this.anchorResource);
				if (simulation != null) {
					try {
						simulation.terminate();
					} catch (Simulation.StaleSimulationException e) {
					}
				}
			}
			
			/* (non-Javadoc)
			 * @see org.apache.wicket.Component#isVisible()
			 */
			@Override
			public boolean isVisible() {
				return isRunning();
			}
			
		});
		
		add(new Label("terminalOutput", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				SimulationModel simulationModel = getSimulationModel();
				if (simulationModel == null) {
					return null;
				}
				if (!(simulationModel.getPrimaryElement() instanceof EcosimPrimaryModelElement)) {
					return null;
				}
				EcosimPrimaryModelElement primaryElement = (EcosimPrimaryModelElement)simulationModel.getPrimaryElement();
				return primaryElement.getTerminalUserInterface().getOutput();
			}
		}));
		
	}
	
	/**
	 * 
	 */
	private SimulationModel getSimulationModel() {
		return (SimulationModel)getDefaultModelObject();
	}

	/**
	 * 
	 */
	private boolean isRunning() {
		return (Simulation.getExisting(EditorPanel.this.anchorResource) != null);
	}
	
}
