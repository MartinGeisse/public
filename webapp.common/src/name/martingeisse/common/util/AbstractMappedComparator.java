/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.Comparator;

/**
 * A comparator that maps the values to compare to other values which are
 * comparable by themselves.
 * @param <T> the type of values being compared by this comparator
 * @param <M> the type of values being compared internally, for example
 * the type of a field of type T
 */
public abstract class AbstractMappedComparator<T, M extends Comparable<? super M>> implements Comparator<T> {
	
	/**
	 * the nullToOther
	 */
	private int nullToOther;
	
	/**
	 * the reverse
	 */
	private boolean reverse;
	
	/**
	 * Constructor.
	 * @param reverse true for reverse order, false for normal order
	 */
	public AbstractMappedComparator(boolean reverse) {
		this(true, reverse);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param nullIsLeast true to treat null as the least value, false to treat
	 * it as the greatest value. Note that the concept of null values
	 * being "least" is applied before the "reverse" flag is taken into account.
	 * For example, non-negative integer values with null being least are ordered:
	 * normal: null, 0, 1, 2, 3
	 * reverse: 3, 2, 1, 0, null
	 * 
	 * @param reverse true for reverse order, false for normal order
	 */
	public AbstractMappedComparator(boolean nullIsLeast, boolean reverse) {
		this.nullToOther = (nullIsLeast ? -1 : 1);
		this.reverse = reverse;
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T o1, T o2) {
		int result = compareNonReversed(o1, o2);
		return (reverse ? -result : result);
	}
	
	/**
	 * 
	 */
	private int compareNonReversed(T o1, T o2) {
		
		// check the input values for null
		if (o1 == o2) {
			return 0;
		} else if (o1 == null) {
			return nullToOther;
		} else if (o2 == null) {
			return -nullToOther;
		}
		
		// map the values
		M mapped1 = map(o1);
		M mapped2 = map(o2);
		
		// check the mapped values for null
		if (mapped1 == mapped2) {
			return 0;
		} else if (mapped1 == null) {
			return nullToOther;
		} else if (mapped2 == null) {
			return -nullToOther;
		}
		
		// compare the mapped values
		return mapped1.compareTo(mapped2);
		
	}
	
	/**
	 * Maps a value of type T to a value of type M.
	 * @param value the value to map
	 * @return the mapped value
	 */
	protected abstract M map(T value);

}
