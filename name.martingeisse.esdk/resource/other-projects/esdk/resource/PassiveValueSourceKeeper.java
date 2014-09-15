/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * Behaves as an {@link IPassiveValueStreamSource} but wraps a
 * continuous {@link IPassiveValueSource}. It contains a current value
 * that is sampled from the underlying source only when
 * produceValue() is called, so the visible value only changes with
 * that function, regardless of value changes from the underlying
 * source.
 * 
 * @param <T> the value type
 */
public class PassiveValueSourceKeeper<T> implements IPassiveValueStreamSource<T> {

	/**
	 * the source
	 */
	private final IPassiveValueSource<T> source;
	
	/**
	 * the value
	 */
	private T value;
	
	/**
	 * Constructor.
	 * @param source the wrapped value source
	 */
	public PassiveValueSourceKeeper(final IPassiveValueSource<T> source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IPassiveValueSource#getValue()
	 */
	@Override
	public T getValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IPassiveValueStreamSource#produceValue()
	 */
	@Override
	public void produceValue() {
		value = source.getValue();
	}

}
