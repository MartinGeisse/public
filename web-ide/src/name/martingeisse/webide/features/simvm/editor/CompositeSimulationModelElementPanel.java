/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.editor;

import name.martingeisse.common.util.NotImplementedException;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.webide.features.ecosim.EcosimPrimaryModelElement;
import name.martingeisse.webide.features.simvm.model.AbstractCompositeSimulationModelElement;
import name.martingeisse.webide.features.simvm.model.SimulationModel;
import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * This panel is used for the UI of {@link AbstractCompositeSimulationModelElement}.
 * It shows a tab panel with one tab for each sub-element.
 * 
 * The panel is also able to show simulator controls in case it is used for
 * a primary model element.
 */
public class CompositeSimulationModelElementPanel extends Panel {

	/**
	 * the simulationModel
	 */
	private final SimulationModel simulationModel;
	
	/**
	 * the showSimulatorControls
	 */
	private final boolean showSimulatorControls;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param simulationModel the simulation model
	 */
	public CompositeSimulationModelElementPanel(String id, SimulationModel simulationModel) {
		this(id, simulationModel, false);
	}
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param simulationModel the simulation model
	 * @param showSimulatorControls whether simulator controls shall be shown
	 */
	public CompositeSimulationModelElementPanel(String id, SimulationModel simulationModel, boolean showSimulatorControls) {
		super(id);
		this.simulationModel = ParameterUtil.ensureNotNull(simulationModel, "simulationModel");
		this.showSimulatorControls = showSimulatorControls;
		if (showSimulatorControls) {
			createRealSimulatorControls();
		} else {
			createDummySimulatorControls();
		}
		if (!showSimulatorControls) {
			throw new NotImplementedException("CompositeSimulationModelElementPanel without simulator controls not yet implemented");
		}
	}
	
	/**
	 * Getter method for the simulationModel.
	 * @return the simulationModel
	 */
	public SimulationModel getSimulationModel() {
		return simulationModel;
	}

	/**
	 * @return the running simulation, or null if not running
	 */
	public SimulatedVirtualMachine getRunningSimulation() {
		return null; // SimulatedVirtualMachine.getExisting(simulationModel.getAnchorResource());
	}	
	
	private void createRealSimulatorControls() {
		add(new Link<Void>("startButton") {

			@Override
			public void onClick() {
				// SimulatedVirtualMachine.getOrCreate(simulationModel.getAnchorResource(), true);
			}

			@Override
			public boolean isVisible() {
				return (getRunningSimulation() == null);
			}

		});
		add(new Link<Void>("terminateButton") {

			@Override
			public void onClick() {
				/*
				final SimulatedVirtualMachine simulation = SimulatedVirtualMachine.getExisting(simulationModel.getAnchorResource());
				if (simulation != null) {
					simulation.terminate();
				}
				*/
			}

			/* (non-Javadoc)
			 * @see org.apache.wicket.Component#isVisible()
			 */
			@Override
			public boolean isVisible() {
				return (getRunningSimulation() != null);
			}

		});
	}

	private void createDummySimulatorControls() {
		// create invisible wicket components here, just so that wicket doesn't complain
	}

}
