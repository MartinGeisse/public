/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.hdl.probe;

import name.martingeisse.esdk.hdl.core.IValueSource;

/**
 * Default probe implementation. Uses {@link #toString()} to turn the
 * probed value into logged text and expects that text to be a
 * one-liner.
 * 
 * @param <T> the type of probed value
 */
public class SimpleProbe<T> extends AbstractProbe<T> {

	/**
	 * Constructor.
	 */
	public SimpleProbe() {
		super();
	}

	/**
	 * Constructor.
	 * @param name the name (for logging the probed value)
	 * @param source the source to probe
	 */
	public SimpleProbe(final String name, final IValueSource<T> source) {
		super(name, source);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.probe.AbstractProbe#logValue(java.lang.String, java.lang.Object)
	 */
	@Override
	protected void logValue(String name, T value) {
		System.out.println(name + ": " + value);
	}

}
