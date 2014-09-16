/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.bus;


/**
 * This class wraps an {@link BusSlave64} and behaves as an {@link BusSlave32}.
 * 
 * Device-local addresses are zero-extended to the 64 bits the device understands.
 * This obviously means that devices with large local address spaces cannot be
 * fully addressed, which is obviously a fundamental problem when accessing such
 * a device over a 32-bit bus.
 * 
 * Values written to the device are zero-extended to 64 bits. There is no way
 * to specify the upper 32 bits with this class; use a specialized bus adapter
 * if that is needed.
 * 
 * Values read from the device are truncated to 32 bits. This class does not
 * provide a way to obtain the upper 32 bits.
 */
public final class BusSlave64AsBusSlave32 implements BusSlave32 {

	/**
	 * the wrapped
	 */
	private BusSlave64 wrapped;

	/**
	 * Constructor.
	 */
	public BusSlave64AsBusSlave32() {
	}

	/**
	 * Constructor.
	 * @param wrapped the wrapped bus slave
	 */
	public BusSlave64AsBusSlave32(final BusSlave64 wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * Getter method for the wrapped bus slave.
	 * @return the wrapped bus slave
	 */
	public BusSlave64 getWrapped() {
		return wrapped;
	}

	/**
	 * Setter method for the wrapped bus slave.
	 * @param wrapped the wrapped bus slave to set
	 */
	public void setWrapped(final BusSlave64 wrapped) {
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IBusSlave32#getLocalAddressBits()
	 */
	@Override
	public int getLocalAddressBits() {
		int n = wrapped.getLocalAddressBits();
		return (n < 32 ? n : 32);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IBusSlave32#read(int)
	 */
	@Override
	public int read(int address) {
		return (int)wrapped.read(zeroExtend(address));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IBusSlave32#write(int, int)
	 */
	@Override
	public void write(int address, int value) {
		wrapped.write(zeroExtend(address), zeroExtend(value));
	}

	/**
	 * Zero-extends the argument to 64 bits.
	 * @param x the value to zero-extended
	 * @return the 64-bit zero-extended argument value
	 */
	private static long zeroExtend(int x) {
		long signExtended = x;
		return (signExtended & 0xFFFFFFFF);
	}
	
}
