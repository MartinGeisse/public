/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.connect;

/**
 * Behaves as an {@link IPassiveValueStreamSink} but wraps a
 * continuous {@link IPassiveValueSink}. It contains a current value
 * that can be changed and read back, but is pushed to the underlying
 * sink only when consumeValue() is called.
 * 
 * @param <T> the value type
 */
public class PassiveValueSinkKeeper<T> implements IPassiveValueStreamSink<T> {

	/**
	 * the sink
	 */
	private final IPassiveValueSink<T> sink;
	
	/**
	 * the value
	 */
	private T value;
	
	/**
	 * Constructor.
	 * @param sink the wrapped value sink
	 */
	public PassiveValueSinkKeeper(final IPassiveValueSink<T> sink) {
		this.sink = sink;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Setter method for the value.
	 * @param value the value to set
	 */
	@Override
	public void setValue(T value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.connect.IPassiveValueStreamSink#consumeValue()
	 */
	@Override
	public void consumeValue() {
		sink.setValue(value);
	}

}
