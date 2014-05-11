/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.iterator;

import java.util.Iterator;

/**
 * This iterator wraps another iterator and returns only elements
 * that are instances of a specific type.
 * 
 * @param <T> the filter type
 */
public class TypeFilteredIterator<T> implements Iterator<T> {

	/**
	 * the wrapped
	 */
	private final Iterator<?> wrapped;

	/**
	 * the filterClass
	 */
	private final Class<T> filterClass;

	/**
	 * the hasNext
	 */
	private boolean hasNext;
	
	/**
	 * the next
	 */
	private T next;
	
	/**
	 * Constructor.
	 * @param wrapped the wrapped iterator
	 * @param filterClass the class object for the filter type
	 */
	public TypeFilteredIterator(final Iterator<?> wrapped, final Class<T> filterClass) {
		this.wrapped = wrapped;
		this.filterClass = filterClass;
		prefetch();
	}

	/**
	 * 
	 */
	private void prefetch() {
		while (true) {
			next = null;
			hasNext = wrapped.hasNext();
			if (!hasNext) {
				return;
			}
			Object element = wrapped.next();
			if (filterClass.isInstance(element)) {
				next = filterClass.cast(element);
				return;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return hasNext;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public T next() {
		T element = next;
		prefetch();
		return element;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		// impossible -- hasNext() has to fetch elements from the wrapped iterator,
		// blocking further calls to wrapped.remove()
		throw new UnsupportedOperationException();
	}

}
