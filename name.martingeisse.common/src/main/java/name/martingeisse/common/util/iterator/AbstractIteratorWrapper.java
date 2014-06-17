/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.iterator;

import java.util.Iterator;

/**
 * This iterator wraps another iterator and returns only elements
 * that are accepted by a subclass-specific filter method. It also
 * allows to map elements to different objects.
 * 
 * The {@link #remove()} method is not supported by this class. To fulfill
 * the iterator contract, this method may have to pre-fetch elements in
 * advance, making a late remove() of the previous element impossible.
 * 
 * @param <S> the element type of the wrapped iterator
 * @param <T> the element type of this iterator
 */
public abstract class AbstractIteratorWrapper<S, T> implements Iterator<T> {

	/**
	 * the wrapped
	 */
	private Iterator<S> wrapped;

	/**
	 * the hasNext
	 */
	private boolean hasNext;
	
	/**
	 * the next
	 */
	private T next;
	
	/**
	 * the skip
	 */
	private boolean skip;
	
	/**
	 * Constructor. Note that before this object can be used, subclasses must invoke
	 * initialize(). Subclass constructors may have to perform additional preparation
	 * before that method is invoked, so this cannot be done in this abstract
	 * constructor.
	 */
	public AbstractIteratorWrapper() {
	}
	
	/**
	 * @param wrapped the wrapped iterator
	 */
	protected final void initialize(final Iterator<S> wrapped) {
		this.wrapped = wrapped;
		prefetch();
	}

	/**
	 * This method is invoked for each element (of type S) from the wrapped iterator.
	 * Subclasses should either return an appropriate element of type T to return
	 * to client code, or call {@link #skip()} to skip the current element. In the
	 * latter case, the return value is ignored.
	 * 
	 * @param element the element from the wrapped iterator
	 * @return the element to return to client code (if not skipped)
	 */
	protected abstract T handleElement(S element);
	
	/**
	 * Skips the current element.
	 */
	protected final void skip() {
		skip = true;
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
			S wrappedElement = wrapped.next();
			skip = false;
			T mappedElement = handleElement(wrappedElement);
			if (!skip) {
				next = mappedElement;
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
