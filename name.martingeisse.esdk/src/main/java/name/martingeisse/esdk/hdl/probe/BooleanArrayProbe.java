/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.hdl.probe;

import name.martingeisse.esdk.hdl.core.IValueSource;

/**
 * Probe implementation for boolean[] values.
 */
public class BooleanArrayProbe extends AbstractProbe<boolean[]> {

	/**
	 * Constructor.
	 */
	public BooleanArrayProbe() {
		super();
	}

	/**
	 * Constructor.
	 * @param name the name (for logging the probed value)
	 * @param source the source to probe
	 */
	public BooleanArrayProbe(final String name, final IValueSource<boolean[]> source) {
		super(name, source);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.probe.AbstractProbe#logValue(java.lang.String, java.lang.Object)
	 */
	@Override
	protected void logValue(final String name, final boolean[] value) {
		System.out.print(name + ": ");
		for (boolean element : value) {
			System.out.print(element ? '1' : '0');
		}
		System.out.println();
	}

}
