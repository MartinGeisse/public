/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.bus;


/**
 * This class wraps an {@link BusSlave32} and behaves as an {@link BusSlave64}.
 * 
 * Device-local addresses are truncated to the 32 bits the device understands.
 * 
 * Values written to the device are truncated to 32 bits.
 * 
 * Values read from the device are zero-extended to 64 bits. If sign-extension
 * is needed, this must be performed either using a specialized bus adapter
 * (instead of this class) or by the master that reads the value.
 */
public final class BusSlave32AsBusSlave64 implements BusSlave64 {

	/**
	 * the wrapped
	 */
	private BusSlave32 wrapped;

	/**
	 * Constructor.
	 */
	public BusSlave32AsBusSlave64() {
	}

	/**
	 * Constructor.
	 * @param wrapped the wrapped bus slave
	 */
	public BusSlave32AsBusSlave64(final BusSlave32 wrapped) {
		this.wrapped = wrapped;
	}

	/**
	 * Getter method for the wrapped bus slave.
	 * @return the wrapped bus slave
	 */
	public BusSlave32 getWrapped() {
		return wrapped;
	}

	/**
	 * Setter method for the wrapped bus slave.
	 * @param wrapped the wrapped bus slave to set
	 */
	public void setWrapped(final BusSlave32 wrapped) {
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
