/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator;

import name.martingeisse.esdk.picoblaze.simulator.port.IPicoblazePortHandler;
import name.martingeisse.esdk.util.IValueSource;

/**
 * An instruction-level simulated PicoBlaze instance.
 */
public class PicoblazeSimulatorBase {

	/**
	 * the instructionMemory
	 */
	private IPicoblazeInstructionMemory instructionMemory;

	/**
	 * the interruptSignal
	 */
	private IValueSource<Boolean> interruptSignal;

	/**
	 * the state
	 */
	private PicoblazeState state;

	/**
	 * Constructor.
	 */
	public PicoblazeSimulatorBase() {
		this.state = new PicoblazeState();
	}

	/**
	 * Getter method for the instructionMemory.
	 * @return the instructionMemory
	 */
	public IPicoblazeInstructionMemory getInstructionMemory() {
		return instructionMemory;
	}

	/**
	 * Setter method for the instructionMemory.
	 * @param instructionMemory the instructionMemory to set
	 */
	public void setInstructionMemory(final IPicoblazeInstructionMemory instructionMemory) {
		this.instructionMemory = instructionMemory;
	}

	/**
	 * Getter method for the interruptSignal.
	 * @return the interruptSignal
	 */
	public IValueSource<Boolean> getInterruptSignal() {
		return interruptSignal;
	}

	/**
	 * Setter method for the interruptSignal.
	 * @param interruptSignal the interruptSignal to set
	 */
	public void setInterruptSignal(final IValueSource<Boolean> interruptSignal) {
		this.interruptSignal = interruptSignal;
	}

	/**
	 * Getter method for the state.
	 * @return the state
	 */
	public PicoblazeState getState() {
		return state;
	}

	/**
	 * Setter method for the state.
	 * @param state the state to set
	 */
	public void setState(final PicoblazeState state) {
		if (state == null) {
			throw new IllegalArgumentException("cannot set the state to null");
		}
		this.state = state;
	}

	/**
	 * Convenience getter method for the portHandler of the state.
	 * @return the portHandler
	 */
	public IPicoblazePortHandler getPortHandler() {
		return state.getPortHandler();
	}

	/**
	 * Convenience setter method for the portHandler of the state.
	 * @param portHandler the portHandler to set
	 */
	public void setPortHandler(IPicoblazePortHandler portHandler) {
		state.setPortHandler(portHandler);
	}
	
}
