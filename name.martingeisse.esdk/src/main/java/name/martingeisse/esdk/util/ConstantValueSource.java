/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.util;

/**
 * An {@link IValueSource} that just returns the same value all the time.
 *
 * @param <T> the type of value returned
 */
public final class ConstantValueSource<T> implements IValueSource<T> {

	/**
	 * the value
	 */
	private final T value;

	/**
	 * Constructor.
	 * @param value the value to return all the time
	 */
	public ConstantValueSource(T value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.util.IValueSource#getValue()
	 */
	@Override
	public T getValue() {
		return value;
	}

}
