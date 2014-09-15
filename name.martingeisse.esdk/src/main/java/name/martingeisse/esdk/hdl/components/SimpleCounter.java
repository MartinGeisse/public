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
public class SimpleCounter implements IClockSignalConsumer {

	/**
	 * the value
	 */
	private int value;
	
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
	 * @param bit ...
	 * @return ...
	 */
	public IValueSource<Boolean> createValueSource(int bit) {
		final int mask = (1 << bit);
		return new IValueSource<Boolean>() {
			
			@Override
			public Boolean getValue() {
				return (value & mask) != 0;
			}
		};
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#computeNextState()
	 */
	@Override
	public void computeNextState() {
		nextValue = value + 1;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#enterNextState()
	 */
	@Override
	public void enterNextState() {
		value = nextValue;
	}

}
