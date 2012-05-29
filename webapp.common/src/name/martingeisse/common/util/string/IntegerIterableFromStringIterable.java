/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import java.util.Iterator;

/**
 * This class implements an integer iterable from a string iterable by parsing each element.
 */
public class IntegerIterableFromStringIterable implements Iterable<Integer> {

	/**
	 * the wrapped
	 */
	private Iterable<String> wrapped;
	
	/**
	 * Constructor.
	 * @param wrapped the wrapped ierable that produces strings.
	 */
	public IntegerIterableFromStringIterable(Iterable<String> wrapped) {
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new IntegerIteratorFromStringIterator(wrapped.iterator());
	}

}
