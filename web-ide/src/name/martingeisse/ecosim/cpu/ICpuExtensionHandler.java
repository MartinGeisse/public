/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu;

/**
 * This handler can be implemented by clients to extend the ECO32
 * CPU in a mostly compatible way.
 */
public interface ICpuExtensionHandler {

	/**
	 * This method is invoked when an illegal opcode has been encountered.
	 * 
	 * It is run in the following context:
	 * - the CPU has not detected an interrupt but instead tries to execute
	 *   an instruction, which has been found to use an illegal opcode
	 * - the PC points to that instruction. It will not be updated after
	 *   this handler runs, so for "normal" instructions, this handler must
	 *   increment the PC by four manually.
	 * 
	 * The handler can either handle the instruction and perform a nonstandard
	 * operation, or signal that it too was unable to handle the opcode. If
	 * no handler is installed, the CPU reacts as if a handler was installed
	 * that simply handles no instructions.
	 * 
	 * @param instruction the instruction code to handle
	 * 
	 * @return true if the instruction was handled. In this case, the PC should
	 * have been updated by the handler as needed; it will not be auto-incremented.
	 * False if the instruction was not handled, leading to an illegal instruction
	 * exception as usual.
	 */
	public boolean handleIllegalOpcode(int instruction);
	
}
