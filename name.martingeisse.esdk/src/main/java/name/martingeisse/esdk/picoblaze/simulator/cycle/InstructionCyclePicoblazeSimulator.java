/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle;

import name.martingeisse.esdk.picoblaze.simulator.core.PicoblazeSimulatorBase;
import name.martingeisse.esdk.picoblaze.simulator.core.PicoblazeSimulatorException;


/**
 * An instruction-level simulated PicoBlaze instance.
 */
public final class InstructionCyclePicoblazeSimulator extends PicoblazeSimulatorBase {

	/**
	 * Performs a single instruction cycle. This either executes one instruction or
	 * performs an interrupt entry, depending on the interrupt signal. If an instruction
	 * is executed, a program memory is needed and used. Depending on the instruction,
	 * a port handler is also needed and used.
	 * 
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performInstructionCycle() throws PicoblazeSimulatorException {
		if (getInterruptSignal() != null && getInterruptSignal().isInterruptPending()) {
			getState().performInterrupt();
		} else if (getInstructionMemory() == null) {
			throw new PicoblazeSimulatorException("no instruction memory");
		} else {
			getState().performInstruction(getInstructionMemory().getInstruction(getState().getPc()));
		}
	}
	
	/**
	 * Performs the specified number of instruction cycles in a loop.
	 * 
	 * @param count the number of instruction cycles to perform
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performMultipleInstructionCycles(int count) throws PicoblazeSimulatorException {
		for (int i=0; i<count; i++) {
			performInstructionCycle();
		}
	}
	
	/**
	 * Performs an infinite number of instruction cycles. The simulator
	 * can only be stopped by throwing an exception in the port handler
	 * or in a custom command.
	 * 
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performInfiniteInstructionCycles() throws PicoblazeSimulatorException {
		while (true) {
			performInstructionCycle();
		}
	}
	
}
