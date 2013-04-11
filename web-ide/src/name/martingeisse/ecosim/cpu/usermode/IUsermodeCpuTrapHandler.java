/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode;

/**
 * This interface gets notified when a TRAP instruction is
 * executed.
 */
public interface IUsermodeCpuTrapHandler {

	/**
	 * Handles a TRAP instruction.
	 */
	public void handleTrap();
	
}
