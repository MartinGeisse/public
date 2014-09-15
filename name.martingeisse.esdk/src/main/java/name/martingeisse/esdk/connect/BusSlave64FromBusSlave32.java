/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * This class wraps an {@link IBusSlave32} and behaves as an {@link IBusSlave64}.
 */
public final class BusSlave64FromBusSlave32 implements IBusSlave64 {

	/**
	 * the wrapped
	 */
	private IBusSlave32 wrapped;

	/**
	 * Constructor.
	 */
	public BusSlave64FromBusSlave32() {
	}

	/**
	 * Constructor.
	 * @param wrapped the wrapped bus slave
	 */
	public BusSlave64FromBusSlave32(final IBusSlave32 wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * Getter method for the wrapped bus slave.
	 * @return the wrapped bus slave
	 */
	public IBusSlave32 getWrapped() {
		return wrapped;
	}

	/**
	 * Setter method for the wrapped bus slave.
	 * @param wrapped the wrapped bus slave to set
	 */
	public void setWrapped(final IBusSlave32 wrapped) {
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IBusSlave64#getLocalAddressBits()
	 */
	@Override
	public int getLocalAddressBits() {
		return wrapped.getLocalAddressBits();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IBusSlave64#read(long)
	 */
	@Override
	public long read(long address) {
		// if the long-typed address is within range, so are its lower 32 bits, even for negative addresses
		long result = wrapped.read((int)address);
		return (result & 0xFFFFFFFF);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IBusSlave64#write(long, long)
	 */
	@Override
	public void write(long address, long value) {
		// if the long-typed address is within range, so are its lower 32 bits, even for negative addresses
		wrapped.write((int)address, (int)value);
	}
	
}
