/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;


/**
 * This interface is used by peripheral devices to signal interrupts.
 */
public interface IInterruptLine {

	/**
	 * Sets the interrupt to active (fire interrupts) or inactive (leave the CPU alone)
	 * @param active whether the line should be active or not
	 */
	public void setActive(boolean active);
	
}
