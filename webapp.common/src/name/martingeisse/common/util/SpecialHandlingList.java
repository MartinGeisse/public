/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This list allows to apply special handling when elements are
 * added or removed.
 * @param <T> the element type
 */
public class SpecialHandlingList<T> implements List<T> {

	/**
	 * the list
	 */
	private final ArrayList<T> list;

	/**
	 * Constructor.
	 */
	public SpecialHandlingList() {
		this.list = new ArrayList<T>();
	}

	/**
	 * Constructor.
	 * @param capacity the initial capacity
	 */
	public SpecialHandlingList(int capacity) {
		this.list = new ArrayList<T>(capacity);
	}

	/**
	 * Constructor.
	 * @param c the collection to initialize from
	 */
	public SpecialHandlingList(Collection<T> c) {
		onBeforeAddElements(c);
		this.list = new ArrayList<T>(c);
		onAfterAddElements(c);
	}

	/**
	 * Helper method.
	 */
	private final void onBeforeAddElements(Iterable<? extends T> elements) {
		onBeforeAddElements(elements.iterator());
	}
	
	/**
	 * Called before the specified elements are added. The default implementation
	 * invokes onBeforeAddElement() for each element.
	 * @param elements the elements being added
	 */
	protected void onBeforeAddElements(Iterator<? extends T> elements) {
		while (elements.hasNext()) {
			onBeforeAddElement(elements.next());
		}
	}

	/**
	 * Called by onBeforeAddElements() for each element by default. The default implementation 
	 * does nothing.
	 * @param element the element being added
	 */
	protected void onBeforeAddElement(T element) {
	}

	/**
	 * Helper method.
	 */
	private final void onAfterAddElements(Iterable<? extends T> elements) {
		onAfterAddElements(elements.iterator());
	}

	/**
	 * Called after the specified elements are added. The default implementation
	 * invokes onAfterAddElement() for each element.
	 * @param elements the elements being added
	 */
	protected void onAfterAddElements(Iterator<? extends T> elements) {
		while (elements.hasNext()) {
			onAfterAddElement(elements.next());
		}
	}

	/**
	 * Called by onAfterAddElements() for each element by default. The default implementation 
	 * does nothing.
	 * @param element the element being added
	 */
	protected void onAfterAddElement(T element) {
	}

	/**
	 * Helper method.
	 */
	private final void onBeforeRemoveElements(Iterable<? extends T> elements) {
		onBeforeRemoveElements(elements.iterator());
	}

	/**
	 * Called before the specified elements are removed. The default implementation
	 * invokes onBeforeRemoveElement() for each element.
	 * @param elements the elements being removed
	 */
	protected void onBeforeRemoveElements(Iterator<? extends T> elements) {
		while (elements.hasNext()) {
			onBeforeRemoveElement(elements.next());
		}
	}

	/**
	 * Called by onBeforeRemoveElements() for each element by default. The default implementation 
	 * does nothing.
	 * @param element the element being removed
	 */
	protected void onBeforeRemoveElement(T element) {
	}

	/**
	 * Helper method.
	 */
	private final void onAfterRemoveElements(Iterable<? extends T> elements) {
		onAfterRemoveElements(elements.iterator());
	}

	/**
	 * Called after the specified elements are removed. The default implementation
	 * invokes onAfterRemoveElement() for each element.
	 * @param elements the elements being removed
	 */
	protected void onAfterRemoveElements(Iterator<? extends T> elements) {
		while (elements.hasNext()) {
			onAfterRemoveElement(elements.next());
		}
	}

	/**
	 * Called by onAfterRemoveElements() for each element by default. The default implementation 
	 * does nothing.
	 * @param element the element being removed
	 */
	protected void onAfterRemoveElement(T element) {
	}

	/* (non-Javadoc)
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	@Override
	public final void add(int index, T element) {
		onBeforeAddElement(element);
		list.add(index, element);
		onAfterAddElement(element);
	}

	/* (non-Javadoc)
	 * @see java.util.List#add(java.lang.Object)
	 */
	@Override
	public final boolean add(T element) {
		onBeforeAddElement(element);
		boolean result = list.add(element);
		onAfterAddElement(element);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	@Override
	public final boolean addAll(Collection<? extends T> elements) {
		onBeforeAddElements(elements);
		boolean result = list.addAll(elements);
		onAfterAddElements(elements);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	@Override
	public final boolean addAll(int index, Collection<? extends T> elements) {
		onBeforeAddElements(elements);
		boolean result = list.addAll(index, elements);
		onAfterAddElements(elements);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.util.List#clear()
	 */
	@Override
	public final void clear() {
		onBeforeRemoveElements(list);
		ArrayList<T> copy = new ArrayList<T>(list);
		list.clear();
		onAfterRemoveElements(copy);
	}

	/* (non-Javadoc)
	 * @see java.util.List#contains(java.lang.Object)
	 */
	@Override
	public final boolean contains(Object o) {
		return list.contains(o);
	}

	/* (non-Javadoc)
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	@Override
	public final boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.List#get(int)
	 */
	@Override
	public final T get(int index) {
		return list.get(index);
	}

	/* (non-Javadoc)
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	@Override
	public final int indexOf(Object o) {
		return list.indexOf(o);
	}

	/* (non-Javadoc)
	 * @see java.util.List#isEmpty()
	 */
	@Override
	public final boolean isEmpty() {
		return list.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.List#iterator()
	 */
	@Override
	public final Iterator<T> iterator() {
		return new MyIterator();
	}

	/* (non-Javadoc)
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	@Override
	public final int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	/* (non-Javadoc)
	 * @see java.util.List#listIterator()
	 */
	@Override
	public final ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.List#listIterator(int)
	 */
	@Override
	public final ListIterator<T> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.List#remove(int)
	 */
	@Override
	public final T remove(int index) {
		T element = list.get(index);
		onBeforeRemoveElement(element);
		list.remove(index);
		onAfterRemoveElement(element);
		return element;
	}

	/* (non-Javadoc)
	 * @see java.util.List#remove(java.lang.Object)
	 */
	@Override
	public final boolean remove(Object o) {
		if (list.contains(o)) {
			onBeforeRemoveElement(GenericTypeUtil.<T>unsafeCast(o));
			boolean result = list.remove(o);
			onAfterRemoveElement(GenericTypeUtil.<T>unsafeCast(o));
			return result;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	@Override
	public final boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	@Override
	public final boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	@Override
	public final T set(int index, T element) {
		T oldElement = list.get(index);
		if (oldElement == element) {
			return oldElement;
		}
		onBeforeRemoveElement(oldElement);
		onBeforeAddElement(element);
		list.set(index, element);
		onAfterRemoveElement(oldElement);
		onAfterAddElement(element);
		return oldElement;
	}

	/* (non-Javadoc)
	 * @see java.util.List#size()
	 */
	@Override
	public final int size() {
		return list.size();
	}

	/* (non-Javadoc)
	 * @see java.util.List#subList(int, int)
	 */
	@Override
	public final List<T> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see java.util.List#toArray()
	 */
	@Override
	public final Object[] toArray() {
		return list.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.List#toArray(T[])
	 */
	@Override
	public final <X> X[] toArray(X[] a) {
		return list.toArray(a);
	}

	/**
	 * Specialized iterator implementation. Currently this just
	 * prevents calling remove().
	 */
	private class MyIterator implements Iterator<T> {

		/**
		 * the inner
		 */
		private Iterator<T> inner;
		
		/**
		 * Constructor.
		 */
		public MyIterator() {
			this.inner = list.iterator();
		}
		
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return inner.hasNext();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public T next() {
			return inner.next();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
	
}
