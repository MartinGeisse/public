/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import name.martingeisse.webide.ipc.IpcEvent;

/**
 * Implementation of {@link IPrimarySimulationModelElement} that simulates
 * a set of {@link IStepwiseSimulationModelElement}s.
 */
public class StepwisePrimarySimulationModelElement implements IPrimarySimulationModelElement {

	/**
	 * the counter
	 */
	private int counter = 0;

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#initialize(name.martingeisse.webide.features.simvm.model.SimulationModel)
	 */
	@Override
	public void initialize(SimulationModel simulationModel) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#singleStep()
	 */
	@Override
	public void singleStep() {
		counter++;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#batchStep()
	 */
	@Override
	public void batchStep() {
		counter++;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#handleEvent(name.martingeisse.webide.ipc.IpcEvent)
	 */
	@Override
	public void handleEvent(IpcEvent<?> event) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#saveState()
	 */
	@Override
	public void saveState() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#loadState()
	 */
	@Override
	public void loadState() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.simvm.model.IPrimarySimulationModelElement#deleteState()
	 */
	@Override
	public void deleteState() {
	}

	/**
	 * Getter method for the counter.
	 * @return the counter
	 */
	public int getCounter() {
		return counter;
	}
	
}
