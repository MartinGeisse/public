/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure;

import name.martingeisse.esdk.util.IValueSource;

/**
 * A module port that transports a value stream from the
 * inside of the module to the outside.
 * 
 * A port is basically an object that decouples the inside
 * from the outside, allowing both sides to deal with swappable
 * {@link IValueSource}s without affecting each other. A port
 * also carries meta-data for debugging and synthesis.
 *
 * @param <T> the type of values transported by this port
 */
public final class OutputPort<T> extends Port<T, OutputPort<T>> {

	/**
	 * the outerValueSource
	 */
	private final IValueSource<T> outerValueSource;

	/**
	 * the innerValueSource
	 */
	private IValueSource<T> innerValueSource;

	/**
	 * Constructor.
	 */
	public OutputPort() {
		this.outerValueSource = new IValueSource<T>() {
			@Override
			public T getValue() {
				return (innerValueSource == null ? null : innerValueSource.getValue());
			}
		};
		this.innerValueSource = null;
	}

	/**
	 * Getter method for the innerValueSource.
	 * @return the innerValueSource
	 */
	public IValueSource<T> getInnerValueSource() {
		return innerValueSource;
	}

	/**
	 * Setter method for the innerValueSource.
	 * @param innerValueSource the innerValueSource to set
	 */
	public void setInnerValueSource(IValueSource<T> innerValueSource) {
		this.innerValueSource = innerValueSource;
	}

	/**
	 * Getter method for the outerValueSource.
	 * @return the outerValueSource
	 */
	public IValueSource<T> getOuterValueSource() {
		return outerValueSource;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.structure.Port#getThis()
	 */
	@Override
	protected OutputPort<T> getThis() {
		return this;
	}
	
}
