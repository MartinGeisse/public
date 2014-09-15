/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.esdk.hdl.probe;

import name.martingeisse.esdk.hdl.core.IClockSignalConsumer;
import name.martingeisse.esdk.hdl.core.IValueSource;

/**
 * 
 */
public class BitWatcher implements IClockSignalConsumer {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the source
	 */
	private IValueSource<Boolean> source;

	/**
	 * Constructor
	 * @param name ...
	 * @param source ...
	 */
	public BitWatcher(String name, IValueSource<Boolean> source) {
		this.name = name;
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#computeNextState()
	 */
	@Override
	public void computeNextState() {
		System.out.println(name + ": " + source.getValue());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#enterNextState()
	 */
	@Override
	public void enterNextState() {
	}

}
