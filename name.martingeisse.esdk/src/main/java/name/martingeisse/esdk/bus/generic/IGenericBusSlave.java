/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.bus.generic;

/**
 * This interface abstracts the generic behavior of bus slave
 * devices. Concrete bus systems may provide convenience methods
 * to attach devices implementing this interface, and concrete
 * devices or wrappers for concrete devices may implement this
 * interface to be attachable in this way.
 * 
 * While the attachment of concrete devices isn't simulated in
 * detail, and while the attachment must be mapped to concrete
 * mechanisms to be synthesizable, using this interface allows
 * connecting devices for vastly different bus types in
 * prototyping and modeling.
 */
public interface IGenericBusSlave {

	/**
	 * Returns the number device-local address bits. All read
	 * and write operations should target addresses that are
	 * representable using this amount of bits. 
	 * 
	 * The effect of operations outside the address range is
	 * undefined. Examples include no effect, aliasing of other
	 * addresses, or throwing an exception.
	 * 
	 * Specifically, let mask := ((1 << getAddressBits) - 1). Then for each
	 * valid address the following holds: ((address & mask) == address).
	 * 
	 * @return the number of address bits.
	 */
	public int getLocalAddressBitCount();
	
	/**
	 * Reads a value from the specified local address.
	 * 
	 * @param localAddress the device-local address to read from
	 * @return the value read
	 */
	public int read(int localAddress);

	/**
	 * Writes a value to the specified localaddress.
	 * 
	 * @param localAddress the device-local address to write to
	 * @param value the value to write
	 */
	public void write(int localAddress, int value);
	
}
