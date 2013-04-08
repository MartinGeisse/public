/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import name.martingeisse.webide.features.simvm.model.SimulatorModel;
import name.martingeisse.webide.features.simvm.simulation.Simulation;

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
	 * Constructor.
	 * @param id the Wicket id
	 * @param model the document model
	 */
	public EditorPanel(String id, IModel<SimulatorModel> model) {
		super(id, model);
		add(new Label("status", new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				Simulation simulation = Simulation.getExisting(getSimulatorModel().getAnchorResource());
				return (simulation == null ? "stopped" : "running");
			}
		}));
		add(new Link<Void>("startButton") {
			@Override
			public void onClick() {
				try {
					Simulation.getOrCreate(getSimulatorModel().getAnchorResource(), true);
				} catch (Simulation.StaleSimulationException e) {
				}
			}
		});
		add(new Link<Void>("terminateButton") {
			@Override
			public void onClick() {
				Simulation simulation = Simulation.getExisting(getSimulatorModel().getAnchorResource());
				if (simulation != null) {
					try {
						simulation.terminate();
					} catch (Simulation.StaleSimulationException e) {
					}
				}
			}
		});
	}
	
	/**
	 * 
	 */
	private SimulatorModel getSimulatorModel() {
		return (SimulatorModel)getDefaultModelObject();
	}
}
