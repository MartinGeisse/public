/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import java.util.Iterator;

/**
 * This class implements an integer iterator from a string iterator by parsing each element.
 */
public class IntegerIteratorFromStringIterator implements Iterator<Integer> {

	/**
	 * the wrapped
	 */
	private Iterator<String> wrapped;
	
	/**
	 * Constructor.
	 * @param wrapped the wrapped iterator that produces strings.
	 */
	public IntegerIteratorFromStringIterator(Iterator<String> wrapped) {
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return wrapped.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Integer next() {
		return Integer.parseInt(wrapped.next().trim());
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		wrapped.remove();
	}

}
