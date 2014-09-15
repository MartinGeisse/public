/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.hdl.probe;

import name.martingeisse.esdk.hdl.core.IClockSignalConsumer;
import name.martingeisse.esdk.hdl.core.IValueSource;

/**
 * Base class for probe implementations.
 * @param <T> the type of probed value
 */
public abstract class AbstractProbe<T> implements IClockSignalConsumer {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the source
	 */
	private IValueSource<T> source;

	/**
	 * the value
	 */
	private T value;

	/**
	 * Constructor.
	 */
	public AbstractProbe() {
	}

	/**
	 * Constructor.
	 * @param name the name (for logging the probed value)
	 * @param source the source to probe
	 */
	public AbstractProbe(final String name, final IValueSource<T> source) {
		this.name = name;
		this.source = source;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the source.
	 * @return the source
	 */
	public IValueSource<T> getSource() {
		return source;
	}

	/**
	 * Setter method for the source.
	 * @param source the source to set
	 */
	public void setSource(final IValueSource<T> source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#computeNextState()
	 */
	@Override
	public final void computeNextState() {
		this.value = source.getValue();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.core.IClockSignalConsumer#enterNextState()
	 */
	@Override
	public final void enterNextState() {
		logValue(name, value);
	}

	/**
	 * Logs a probed value.
	 * @param name the name to use for logging
	 * @param value the probed value
	 */
	protected abstract void logValue(String name, T value);
	
}
