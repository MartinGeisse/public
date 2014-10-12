/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure;

import name.martingeisse.esdk.util.IValueSource;

/**
 * A module port that transports a value stream from the
 * outside of the module into the inside.
 * 
 * A port is basically an object that decouples the inside
 * from the outside, allowing both sides to deal with swappable
 * {@link IValueSource}s without affecting each other. A port
 * also carries meta-data for debugging and synthesis.
 *
 * @param <T> the type of values transported by this port
 */
public final class InputPort<T> extends Port<T, InputPort<T>> {

	/**
	 * the innerValueSource
	 */
	private final IValueSource<T> innerValueSource;
	
	/**
	 * the outerValueSource
	 */
	private IValueSource<T> outerValueSource;

	/**
	 * Constructor.
	 */
	public InputPort() {
		this.innerValueSource = new IValueSource<T>() {
			@Override
			public T getValue() {
				return (outerValueSource == null ? null : outerValueSource.getValue());
			}
		};
		this.outerValueSource = null;
	}

	/**
	 * Getter method for the innerValueSource.
	 * @return the innerValueSource
	 */
	public IValueSource<T> getInnerValueSource() {
		return innerValueSource;
	}

	/**
	 * Getter method for the outerValueSource.
	 * @return the outerValueSource
	 */
	public IValueSource<T> getOuterValueSource() {
		return outerValueSource;
	}

	/**
	 * Setter method for the outerValueSource.
	 * @param outerValueSource the outerValueSource to set
	 */
	public void setOuterValueSource(IValueSource<T> outerValueSource) {
		this.outerValueSource = outerValueSource;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.structure.Port#getThis()
	 */
	@Override
	protected InputPort<T> getThis() {
		return this;
	}
	
}
