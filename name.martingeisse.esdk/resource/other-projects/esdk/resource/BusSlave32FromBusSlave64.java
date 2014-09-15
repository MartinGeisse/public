/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * This class wraps an {@link IBusSlave64} and behaves as an {@link IBusSlave32}.
 */
public final class BusSlave32FromBusSlave64 implements IBusSlave32 {

	/**
	 * the wrapped
	 */
	private IBusSlave64 wrapped;

	/**
	 * Constructor.
	 */
	public BusSlave32FromBusSlave64() {
	}

	/**
	 * Constructor.
	 * @param wrapped the wrapped bus slave
	 */
	public BusSlave32FromBusSlave64(final IBusSlave64 wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * Getter method for the wrapped bus slave.
	 * @return the wrapped bus slave
	 */
	public IBusSlave64 getWrapped() {
		return wrapped;
	}

	/**
	 * Setter method for the wrapped bus slave.
	 * @param wrapped the wrapped bus slave to set
	 */
	public void setWrapped(final IBusSlave64 wrapped) {
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
