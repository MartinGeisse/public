/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.ecosim;

import name.martingeisse.webide.features.simvm.editor.CompositeSimulationModelElementPanel;
import name.martingeisse.webide.features.simvm.editor.SimulatorControlPanel;
import name.martingeisse.webide.features.simvm.model.ICompositeSimulationModelElement;
import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * The panel used for the Ecosim UI. This is the component returned for the primary
 * model element.
 */
public class EcosimPanel extends CompositeSimulationModelElementPanel<IEcosimModelElement> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	@SuppressWarnings("unchecked")
	public EcosimPanel(String id, IModel<EcosimPrimaryModelElement> model) {
		super(id, convertModel(model));
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private static IModel<ICompositeSimulationModelElement<IEcosimModelElement>> convertModel(IModel<?> model) {
		return (IModel<ICompositeSimulationModelElement<IEcosimModelElement>>)model;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.editor.CompositeSimulationModelElementPanel#getModel()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IModel<EcosimPrimaryModelElement> getModel() {
		return (IModel<EcosimPrimaryModelElement>)getDefaultModel();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.editor.CompositeSimulationModelElementPanel#getSimulationModelElement()
	 */
	@Override
	public EcosimPrimaryModelElement getSimulationModelElement() {
		return getModel().getObject();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.editor.CompositeSimulationModelElementPanel#createRightPanel(java.lang.String)
	 */
	@Override
	protected Component createRightPanel(String id) {
		return new SimulatorControlPanel(id, new PropertyModel<SimulatedVirtualMachine>(getModel(), "simulationModel.virtualMachine"));
	}
	
}
