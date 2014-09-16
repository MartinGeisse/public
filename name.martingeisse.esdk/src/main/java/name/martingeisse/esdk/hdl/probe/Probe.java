/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.hdl.probe;

import name.martingeisse.esdk.hdl.core.IValueSource;
import name.martingeisse.esdk.hdl.probe.formatter.DefaultValueFormatter;
import name.martingeisse.esdk.hdl.probe.formatter.ValueFormatter;

/**
 * Default probe implementation that uses a {@link ValueFormatter} to
 * turn each value to a string and System.out to log them.
 * 
 * @param <T> the type of probed value
 */
public final class Probe<T> extends AbstractProbe<T> {

	/**
	 * the formatter
	 */
	private ValueFormatter<? super T> formatter;

	/**
	 * Constructor.
	 * @param name the name (for logging the probed value)
	 * @param source the source to probe
	 * @param formatter the formatter that turns the value to a string
	 */
	public Probe(final String name, final IValueSource<? extends T> source) {
		this(name, source, new DefaultValueFormatter());
	}

	/**
	 * Constructor.
	 * @param name the name (for logging the probed value)
	 * @param source the source to probe
	 * @param formatter the formatter that turns the value to a string
	 */
	public Probe(final String name, final IValueSource<? extends T> source, ValueFormatter<? super T> formatter) {
		super(name, source);
		this.formatter = formatter;
	}

	/**
	 * Getter method for the formatter.
	 * @return the formatter
	 */
	public ValueFormatter<? super T> getFormatter() {
		return formatter;
	}

	/**
	 * Setter method for the formatter.
	 * @param formatter the formatter to set
	 */
	public void setFormatter(ValueFormatter<? super T> formatter) {
		this.formatter = formatter;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.hdl.probe.AbstractProbe#logValue(java.lang.String, java.lang.Object)
	 */
	@Override
	protected void logValue(String name, T value) {
		System.out.println(name + ": " + formatter.formatValue(value));
	}

}
