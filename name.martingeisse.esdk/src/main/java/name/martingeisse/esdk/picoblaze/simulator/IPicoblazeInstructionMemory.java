/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator;


/**
 * A model for the instruction memory used by the PicoBlaze.
 */
public interface IPicoblazeInstructionMemory {

	/**
	 * Fetches the instruction for the specified address.
	 * @param address the instruction address, typically taken from the PicoBlaze's
	 * program counter. Must be in the range (0..1023).
	 * @return the instruction
	 * @throws PicoblazeSimulatorException if this model fails
	 */
	public int getInstruction(int address) throws PicoblazeSimulatorException;
	
}
