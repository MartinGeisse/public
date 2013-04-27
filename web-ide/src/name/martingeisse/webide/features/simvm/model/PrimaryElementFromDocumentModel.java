/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import name.martingeisse.webide.document.Document;
import name.martingeisse.webide.features.simvm.simulation.SimulatedVirtualMachine;
import name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel;

import org.apache.wicket.model.IModel;

/**
 * Maps between the {@link IModel}s for {@link Document} and
 * the primary element, represented as {@link ISimulationModelElement}.
 */
public final class PrimaryElementFromDocumentModel extends AbstractReadOnlyTransformationModel<ISimulationModelElement, Document> {

	/**
	 * Constructor.
	 * @param wrappedModel the model for the {@link Document}.
	 */
	public PrimaryElementFromDocumentModel(final IModel<Document> wrappedModel) {
		super(wrappedModel);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.util.AbstractReadOnlyTransformationModel#transformValue(java.lang.Object)
	 */
	@Override
	protected IPrimarySimulationModelElement transformValue(final Document document) {
		SimulatedVirtualMachine virtualMachine = (SimulatedVirtualMachine)document.getBody();
		return virtualMachine.getSimulationModel().getPrimaryElement();
	}

}
