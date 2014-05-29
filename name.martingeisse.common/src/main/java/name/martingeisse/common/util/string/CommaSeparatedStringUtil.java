/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.string;

import java.util.Iterator;

/**
 * Utility methods for strings that contain a list of comma-separated substrings.
 */
public class CommaSeparatedStringUtil {

	/**
	 * Creates an iterable for the comma-separated substrings in the specified value string.
	 * @param value the value string that contains comma-separated substrings
	 * @return the iterable for the substrings
	 */
	public static Iterable<String> createIterable(String value) {
		return new AbstractStringFragmentIterable(value) {
			@Override
			public Iterator<String> iterator() {
				return new AbstractStringFragmentIterator(getValue()) {
					
					@Override
					protected boolean moveToNext() {
						
						// skip to the first character or skip past the comma
						int p = getEndPosition() + 1;
						if (p > getValue().length()) {
							return false;
						}
						
						// find the next comma
						int q = getValue().indexOf(',', p);
						
						// get the next fragment
						setStartPosition(p);
						setEndPosition((q == -1) ? getValue().length() : q);
						return true;
						
					}
					
					@Override
					public boolean hasNext() {
						return (getEndPosition() < getValue().length());
					}
					
				};
			}
		};
	}

	/**
	 * Creates an iterable for the parsed values of comma-separated decimal integer
	 * number substrings in the specified value string.
	 * @param value the value string that contains comma-separated decimal integers
	 * @return the iterable for the parsed integers
	 */
	public static Iterable<Integer> createIntegerIterable(String value) {
		if (value.isEmpty()) {
			return new EmptyIterable<Integer>();
		} else {
			return new IntegerIterableFromStringIterable(createIterable(value));
		}
	}

	/**
	 * Appends the specified element string, separated by a comma, unless either
	 * the element is equal to one of the comma-separated elements already present
	 * in the set string, or the combined string is longer than the specified maximum length.
	 * @param set the original set string
	 * @param elementToAppend the element to append
	 * @param maxLength the maximum length, or null for no maximum length
	 * @return the potentially modified set string
	 */
	public static String appendIfNotContained(String set, String elementToAppend, Integer maxLength) {
		String combined = appendIfNotContained(set, elementToAppend);
		return ((maxLength != null && combined.length() > maxLength) ? set : combined);
	}
	
	/**
	 * Appends the specified element string, separated by a comma, unless it is equal
	 * to one of the comma-separated elements already present in the set string.
	 * @param set the original set string
	 * @param elementToAppend the element to append
	 * @return the potentially modified set string
	 */
	public static String appendIfNotContained(String set, String elementToAppend) {
		if (set == null) {
			throw new IllegalArgumentException("'set' is null");
		}
		if (elementToAppend == null) {
			throw new IllegalArgumentException("'elementToAppend' is null");
		}
		if (elementToAppend.isEmpty()) {
			throw new IllegalArgumentException("'elementToAppend' is empty");
		}
		if (elementToAppend.indexOf(',') != -1) {
			throw new IllegalArgumentException("'elementToAppend' contains a comma");
		}
		
		for (String existingElement : createIterable(set)) {
			if (existingElement.equals(elementToAppend)) {
				return set;
			}
		}
		if (set.isEmpty()) {
			return elementToAppend;
		} else {
			return set + ',' + elementToAppend;
		}
	}

	/**
	 * This method works like joinSets(set1, set2), but considers null to represent
	 * an empty set, just like the empty string.
	 * 
	 * @param set1 the first set string
	 * @param set2 the second set string
	 * @return the joined set string
	 */
	public static String joinSetsNullSafe(String set1, String set2) {
		return joinSetsNullSafe(set1, set2, null);
	}
	
	/**
	 * This method works like joinSets(set1, set2, maxLength), but considers null to represent
	 * an empty set, just like the empty string.
	 * 
	 * @param set1 the first set string
	 * @param set2 the second set string
	 * @param maxLength the maximum length, or null for no maximum length
	 * @return the joined set string
	 */
	public static String joinSetsNullSafe(String set1, String set2, Integer maxLength) {
		return joinSets(set1 == null ? "" : set1, set2 == null ? "" : set2, maxLength);
	}
	
	/**
	 * Joins two set strings, i.e. strings that are expected to contain no duplicate
	 * values. The result is a set string that contains each value exactly once that
	 * is contained in one or both the input strings.
	 * 
	 * @param set1 the first set string
	 * @param set2 the second set string
	 * @param maxLength the maximum length
	 * @return the joined set string
	 */
	public static String joinSets(String set1, String set2, Integer maxLength) {
		if (set1 == null) {
			throw new IllegalArgumentException("set1 is null");
		}
		if (set2 == null) {
			throw new IllegalArgumentException("set2 is null");
		}
		if (set1.isEmpty()) {
			return set2;
		}
		if (set2.isEmpty()) {
			return set1;
		}

		String result = set1;
		for (String element : createIterable(set2)) {
			result = appendIfNotContained(result, element, maxLength);
		}
		return result;
	}
	
	/**
	 * Joins two set strings, i.e. strings that are expected to contain no duplicate
	 * values. The result is a set string that contains each value exactly once that
	 * is contained in one or both the input strings.
	 * 
	 * @param set1 the first set string
	 * @param set2 the second set string
	 * @return the joined set string
	 */
	public static String joinSets(String set1, String set2) {
		return joinSets(set1, set2, null);
	}
	
}
