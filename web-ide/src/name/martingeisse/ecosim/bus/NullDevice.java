/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

/**
 * This device does absolutely nothing and uses a configurable number
 * of address bits and interrupt lines.
 */
public class NullDevice extends AbstractPeripheralDevice {

	/**
	 * the localAddressBitCount
	 */
	private int localAddressBitCount;

	/**
	 * the interruptLineCount
	 */
	private int interruptLineCount;

	/**
	 * Constructor
	 * @param localAddressBitCount the number of device-local address bits
	 * @param interruptLineCount the number of interrupt lines
	 */
	public NullDevice(int localAddressBitCount, int interruptLineCount) {
		super();
		this.localAddressBitCount = localAddressBitCount;
		this.interruptLineCount = interruptLineCount;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) throws BusTimeoutException {
		return 0;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) throws BusTimeoutException {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.IInterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return interruptLineCount;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return localAddressBitCount;
	}

}
