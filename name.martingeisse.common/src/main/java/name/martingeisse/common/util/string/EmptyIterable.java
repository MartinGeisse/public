/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import java.util.Iterator;

/**
 * This iterable produces no elements.
 * @param <T> the element type
 */
public class EmptyIterable<T> implements Iterable<T> {

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new EmptyIterator<T>();
	}

}
