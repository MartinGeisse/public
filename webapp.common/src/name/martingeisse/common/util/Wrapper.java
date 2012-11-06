/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

/**
 * A simple class that contains a reference to another object. This
 * is useful whenever a nullable reference must be stored in a
 * place where null is not allowed.
 * 
 * @param <T> the type of the wrapped value
 */
public final class Wrapper<T> {

	/**
	 * the value
	 */
	private T value;
	
	/**
	 * Constructor.
	 */
	public Wrapper() {
	}
	
	/**
	 * Constructor.
	 * @param value the value
	 */
	public Wrapper(T value) {
		this.value = value;
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
	public void setValue(T value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj instanceof Wrapper<?>) {
			Wrapper<?> other = (Wrapper<?>)obj;
			return (value == other.value ? true : value == null ? false : value.equals(other.value));
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (value == null ? 0 : value.hashCode());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (value == null ? "null" : value.toString());
	}
	
	/**
	 * Factory method to avoid specifying the type parameter.
	 * @param value the value to wrap
	 * @return the wrapper
	 */
	public static <T> Wrapper<T> of(T value) {
		return new Wrapper<T>(value);
	}
	
}
