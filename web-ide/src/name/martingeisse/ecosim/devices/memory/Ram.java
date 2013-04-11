/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.memory;

import name.martingeisse.ecosim.bus.BusAccessSize;
import name.martingeisse.ecosim.bus.BusTimeoutException;

/**
 * RAM device.
 */
public class Ram extends AbstractMemory {

	/**
	 * Constructor
	 * @param localAddressBits the number of memory-local address bits. This
	 * automatically determines the memory's size.
	 */
	public Ram(int localAddressBits) {
		super(localAddressBits);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#write(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize, int)
	 */
	@Override
	public void write(int localAddress, BusAccessSize size, int value) throws BusTimeoutException {
		switch (size) {

		case BYTE:
			writeByte(localAddress, value);
			break;
			
		case HALFWORD:
			writeByte(localAddress, value >> 8);
			writeByte(localAddress + 1, value);
			break;
			
		case WORD:
			writeByte(localAddress, value >> 24);
			writeByte(localAddress + 1, value >> 16);
			writeByte(localAddress + 2, value >> 8);
			writeByte(localAddress + 3, value);
			break;
			
		default:
			throw new IllegalArgumentException("invalid bus access size: " + size);
			
		}		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ram";
	}

}
