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
	 * the state
	 */
	private final PicoblazeState state;

	/**
	 * the instructionMemory
	 */
	private IPicoblazeInstructionMemory instructionMemory;

	/**
	 * the interruptSignal
	 */
	private IValueSource<Boolean> interruptSignal;

	/**
	 * Constructor.
	 */
	public PicoblazeSimulatorBase() {
		this.state = new PicoblazeState();
	}

	/**
	 * Getter method for the state.
	 * @return the state
	 */
	public PicoblazeState getState() {
		return state;
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

	/**
	 * Getter method for the magicInstructionHandler.
	 * @return the magicInstructionHandler
	 */
	public IMagicInstructionHandler getMagicInstructionHandler() {
		return state.getMagicInstructionHandler();
	}

	/**
	 * Setter method for the magicInstructionHandler.
	 * @param magicInstructionHandler the magicInstructionHandler to set
	 */
	public void setMagicInstructionHandler(IMagicInstructionHandler magicInstructionHandler) {
		state.setMagicInstructionHandler(magicInstructionHandler);
	}

}
