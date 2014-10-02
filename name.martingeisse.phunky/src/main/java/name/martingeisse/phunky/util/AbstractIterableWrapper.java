/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.util;

import java.util.Iterator;

/**
 * Like {@link AbstractIteratorWrapper}, but for {@link Iterable}.
 * 
 * @param <S> the element type of the wrapped iterator
 * @param <T> the element type of this iterator
 */
public abstract class AbstractIterableWrapper<S, T> implements Iterable<T> {

	/**
	 * the wrapped
	 */
	private final Iterable<S> wrapped;
	
	/**
	 * Constructor.
	 * @param wrapped the wrapped iterable
	 */
	public AbstractIterableWrapper(Iterable<S> wrapped) {
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		AbstractIteratorWrapper<S, T> iteratorWrapper = new AbstractIteratorWrapper<S, T>() {
			@Override
			protected T handleElement(S element) {
				return AbstractIterableWrapper.this.handleElement(element);
			}
		};
		iteratorWrapper.initialize(wrapped.iterator());
		return iteratorWrapper;
	}

	/**
	 * This method is invoked for each element (of type S) from the wrapped iterator.
	 * Subclasses should either return an appropriate element of type T to return
	 * to client code, or call {@link #skip(AbstractIteratorWrapper)} to skip the
	 * current element. In the latter case, the return value is ignored.
	 * 
	 * @param element the element from the wrapped iterator
	 * @return the element to return to client code (if not skipped)
	 */
	protected abstract T handleElement(S element);

	/**
	 * Skips the current element in the specified iterator.
	 */
	protected final void skip(AbstractIteratorWrapper<S, T> iterator) {
		iterator.skip();
	}

}
