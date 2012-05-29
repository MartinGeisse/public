/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import java.util.Iterator;

/**
 * Abstract superclass for iterables that produce substrings of a given value string.
 */
public abstract class AbstractStringFragmentIterable implements Iterable<String> {

	/**
	 * the value
	 */
	private final String value;

	/**
	 * Constructor.
	 * @param value the value string from which substrings are iterated.
	 */
	public AbstractStringFragmentIterable(final String value) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		this.value = value;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public abstract Iterator<String> iterator();

}
