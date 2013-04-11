/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.memory;

import name.martingeisse.ecosim.bus.BusAccessSize;
import name.martingeisse.ecosim.bus.BusTimeoutException;

/**
 * Read-only memory. Attempting to write to this memory will result
 * in a bus timeout exception.
 */
public class Rom extends AbstractMemory {

	/**
	 * Constructor
	 * @param localAddressBits the number of memory-local address bits. This
	 * automatically determines the memory's size.
	 */
	public Rom(int localAddressBits) {
		super(localAddressBits);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#write(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize, int)
	 */
	@Override
	public void write(int localAddress, BusAccessSize size, int value) throws BusTimeoutException {
		throw new BusTimeoutException("trying to write to the ROM");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "rom";
	}

}
