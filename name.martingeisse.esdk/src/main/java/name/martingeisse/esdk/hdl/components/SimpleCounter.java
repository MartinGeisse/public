/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.components;

import name.martingeisse.esdk.hdl.core.IClockSignalConsumer;
import name.martingeisse.esdk.hdl.core.IValueSource;

/**
 * A 32-bit counter that allows to get value sources for
 * subranges of its bits. In other words, this counter is not
 * created with a specific width, but rather value sources
 * are created taking a specific width from the counter.
 */
public final class SimpleCounter implements IClockSignalConsumer {

	/**
	 * the currentValue
	 */
	private int currentValue;
	
	/**
	 * the nextValue
	 */
	private int nextValue;

	/**
	 * Constructor
	 */
	public SimpleCounter() {
	}

	/**
	 * Returns a value source for the full counter value.
	 * @return the value source
	 */
	public IValueSource<Integer> getValueSource() {
		return new IValueSource<Integer>() {
			@Override
			public Integer getValue() {
				return currentValue;
			}
		};
	}

	/**
	 * Returns a value source for a single bit of the counter.
	 * 
	 * @param bit the bit index
	 * @return the value source
	 */
	public IValueSource<Boolean> getBitValueSource(int bit) {
		final int mask = (1 << bit);
		return new IValueSource<Boolean>() {
			@Override
			public Boolean getValue() {
				return (currentValue & mask) != 0;
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#computeNextState()
	 */
	@Override
	public void computeNextState() {
		nextValue = currentValue + 1;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#enterNextState()
	 */
	@Override
	public void enterNextState() {
		currentValue = nextValue;
	}

}
