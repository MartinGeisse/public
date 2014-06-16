/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.util;

import java.util.Iterator;

import org.apache.commons.collections.iterators.UnmodifiableIterator;

/**
 * This class wraps another iterator to block off the {@link #remove()} method.
 * It is like {@link UnmodifiableIterator}, just with a type parameter.
 */
public final class ImmutableIteratorWrapper<T> implements Iterator<T> {

	/**
	 * the wrapped
	 */
	private final Iterator<T> wrapped;

	/**
	 * Constructor.
	 * @param wrapped the wrapped iterator
	 */
	public ImmutableIteratorWrapper(Iterator<T> wrapped) {
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
	public T next() {
		return wrapped.next();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
