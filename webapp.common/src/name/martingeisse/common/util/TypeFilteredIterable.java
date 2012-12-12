/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.Iterator;

/**
 * This iterable wraps another iterable and returns only elements
 * that are instances of a specific type.
 * 
 * @param <T> the filter type
 */
public class TypeFilteredIterable<T> implements Iterable<T> {

	/**
	 * the wrapped
	 */
	private final Iterable<?> wrapped;
	
	/**
	 * the filterClass
	 */
	private final Class<T> filterClass;
	
	/**
	 * Constructor.
	 * @param wrapped the wrapped iterable
	 * @param filterClass the class object for the filter type
	 */
	public TypeFilteredIterable(Iterable<?> wrapped, Class<T> filterClass) {
		this.wrapped = wrapped;
		this.filterClass = filterClass;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return new TypeFilteredIterator<T>(wrapped.iterator(), filterClass);
	}
	
}
