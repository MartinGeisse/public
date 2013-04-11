/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * This abstract implementation ensures that only word-sized bus
 * operations are allowed.
 */
public abstract class AbstractPeripheralDevice implements IPeripheralDevice {

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#read(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize)
	 */
	@Override
	public int read(int localAddress, BusAccessSize size) throws BusTimeoutException {
		if (size == BusAccessSize.WORD) {
			return readWord(localAddress);
		} else {
			throw new RuntimeException("access to device " + this + " must be word-sized");
		}
	}
	
	/**
	 * @param localAddress the local address to read
	 * @return Returns the value for that address.
	 */
	protected abstract int readWord(int localAddress) throws BusTimeoutException;

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.PeripheralDevice#write(int, name.martingeisse.ecotools.simulator.bus.BusAccessSize, int)
	 */
	@Override
	public void write(int localAddress, BusAccessSize size, int value) throws BusTimeoutException {
		if (size == BusAccessSize.WORD) {
			writeWord(localAddress, value);
		} else {
			throw new RuntimeException("access to device " + this + " must be word-sized");
		}
	}

	/**
	 * @param localAddress the local address to write
	 * @param value the value to write
	 */
	protected abstract void writeWord(int localAddress, int value) throws BusTimeoutException;

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#dispose()
	 */
	@Override
	public void dispose() {
	}
	
}
