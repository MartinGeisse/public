/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator;


/**
 * An instruction-level simulated PicoBlaze instance.
 */
public final class InstructionLevelPicoblazeSimulator extends PicoblazeSimulatorBase {

	/**
	 * Performs a single instruction cycle. This either executes one instruction or
	 * performs an interrupt entry, depending on the interrupt signal. If an instruction
	 * is executed, a program memory is needed and used. Depending on the instruction,
	 * a port handler is also needed and used.
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performInstructionCycle() throws PicoblazeSimulatorException {
		if (getInterruptSignal() != null && getInterruptSignal().getValue()) {
			getState().performInterrupt();
		} else if (getInstructionMemory() == null) {
			throw new PicoblazeSimulatorException("no instruction memory");
		} else {
			getState().performInstruction(getInstructionMemory().getInstruction(getState().getPc()));
		}
	}
	
	/**
	 * Performs the specified number of instruction cycles.
	 * @param count the number of instruction cycles to perform
	 * @throws PicoblazeSimulatorException when this model fails
	 */
	public void performMultipleInstructionCycles(int count) throws PicoblazeSimulatorException {
		for (int i=0; i<count; i++) {
			performInstructionCycle();
		}
	}
	
}
