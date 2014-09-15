/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.genericbus;

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
	 * Returns the number address bits. All read and write operations
	 * should target addresses that are representable using this amount
	 * of bits. The effect of operations outside this range is undefined.
	 * Examples include no effect, aliasing of other addresses, or throwing
	 * an exception.
	 * 
	 * Specifically, let mask := ((1 << getAddressBits) - 1). Then for each
	 * valid address the following holds: ((address & mask) == address).
	 * 
	 * @return the number of address bits.
	 */
	public int getAddressBitCount();
	
	/**
	 * Reads a value from the specified address.
	 * @param address the address to read from
	 * @return the value read
	 */
	public int read(int address);

	/**
	 * Writes a value to the specified address.
	 * @param address the address to write to
	 * @param value the value to write
	 */
	public void write(int address, int value);
	
}
