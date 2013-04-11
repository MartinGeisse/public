/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.bus;

import name.martingeisse.ecosim.timer.ITickable;

/**
 * This interface represents a device that is connected to the CPU
 * via the bus and optionally one or more interrupt lines.
 */
public interface IPeripheralDevice extends ITickable {

	/**
	 * @return Returns the number of local address bits for this device.
	 * This allows to detect errors in the bus wiring that would cause
	 * local address bits of the device to be left unconnected, leading to
	 * unreachable device addresses.
	 */
	public int getLocalAddressBitCount();
	
	/**
	 * Reads a value from the device.
	 * @param localAddress the device-local address to read from.
	 * @param size the access size
	 * @return Returns the value read from the device.
	 * @throws BusTimeoutException if the specified local address is not connected
	 */
	public int read(int localAddress, BusAccessSize size) throws BusTimeoutException;
	
	/**
	 * Writes a value to the device.
	 * @param localAddress the device-local address to write to.
	 * @param size the access size
	 * @param value the value to write to the device.
	 * @throws BusTimeoutException if the specified local address is not connected
	 */
	public void write(int localAddress, BusAccessSize size, int value) throws BusTimeoutException;
	
	/**
	 * @return Returns the number of interrupt lines this device uses.
	 * The return value is used for the size of the array of interrupt
	 * lines passed to connectInterruptLines().
	 */
	public int getInterruptLineCount();

	/**
	 * @param interruptLines the interrupt lines to connect to the device.
	 * The number of lines is determined by getInterruptLineCount(). The
	 * meaning of the lines is defined by the device.
	 */
	public void connectInterruptLines(IInterruptLine[] interruptLines);

	/**
	 * Performs any clean-up work needed for this device.
	 */
	public void dispose();
	
}
