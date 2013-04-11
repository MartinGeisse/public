/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu;

/**
 * This interface is used by the CPU to create a performance profile.
 */
public interface ICpuProfiler {

	/**
	 * Records that an instruction was executed from the specified address.
	 * @param address the instruction address
	 */
	public void recordInstructionAddress(int address);
	
}
