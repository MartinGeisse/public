/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * This interface represents the CPU's view on the bus.
 */
public interface IBusMasterAccess {

	/**
	 * Reads a value from the bus.
	 * @param address the address to read from.
	 * @param size the access size
	 * @return Returns the value read from the bus.
	 * @throws BusErrorException if a bus error occurs
	 */
	public int read(int address, BusAccessSize size) throws BusErrorException;

	/**
	 * Writes a value to the bus.
	 * @param address the address to write to.
	 * @param size the access size
	 * @param value the value to write
	 * @throws BusErrorException if a bus error occurs
	 */
	public void write(int address, BusAccessSize size, int value) throws BusErrorException;

	/**
	 * @param mask the interrupt mask. 0-bits indicate interrupt lines that are invisible even when
	 * active. 1-bits indicate visible interrupt lines whose activity can be observed normally.
	 * @return Returns the index of the smallest active interrupt line that is visible according
	 * to the specified interrupt mask, or -1 if no such line is active.
	 */
	public int getActiveInterrupt(int mask);

}