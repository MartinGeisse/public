/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.bus;

/**
 * This interface is used by devices that can be connected as
 * a slave device to a 64-bit bus.
 */
public interface BusSlave64 {

	/**
	 * Returns the number of device-local address bits.
	 * Local addresses passed to read() and write() must
	 * conform to this number of bits.
	 * 
	 * Formally, if
	 *   localMask := ((1 << getLocalAddressBits()) - 1),
	 * then local addresses must be fully covered by that mask:
	 *   (localAddress == (localAddress & localMask)).
	 * 
	 * @return the number of device-local address bits, in the range 0..64
	 */
	public int getLocalAddressBits();
	
	/**
	 * Handles a read operation from the specified address.
	 * @param localAddress the device-local address to read from
	 * @return the value read
	 */
	public long read(long localAddress);

	/**
	 * Handles a write operation to the specified address.
	 * @param localAddress the device-local address to read from
	 * @param value the value to write
	 */
	public void write(long localAddress, long value);
	
}
