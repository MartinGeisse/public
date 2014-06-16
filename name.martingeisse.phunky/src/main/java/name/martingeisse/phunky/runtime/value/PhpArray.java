/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import name.martingeisse.common.util.ImmutableIteratorWrapper;
import name.martingeisse.phunky.runtime.ErrorReporter;

/**
 * PHP Array. This is a key/value map with an additional key order.
 * 
 * Feature list:
 * 
 * - key/value map using string keys. PHP key behavior can be reduced
 *   to string-only keys, with a bit of special behavior for keys that
 *   can be parsed as integers. Specifically, 42 and "42" are considered
 *   to be the same key.
 *   
 * - missing entries are different from entries whose value is null
 * 
 * - key order. All entries have an order that by default is the
 *   insertion order, but can be manipulated separately. Specifically,
 *   this separate order affects numeric and non-numeric indices
 *   equally, and is separate from the numeric order on numeric indices.
 *   
 * - remembers the highest-used numeric index to generate new indices
 *   when appending elements
 */
public final class PhpArray {

	/**
	 * the keyValueMap
	 */
	private final HashMap<String, Object> keyValueMap = new HashMap<>();
	
	/**
	 * the keyList
	 */
	private final ArrayList<String> keyList = new ArrayList<>();
	
	/**
	 * the highestNumericIndexUsed
	 */
	private int highestNumericIndexUsed = -1;
	
	/**
	 * Checks whether this array is empty.
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return keyList.isEmpty();
	}

	/**
	 * Appends a value to this array. This is the same as {@link #put(String, Object)},
	 * using the highest used integer index so far, plus one, as the key. Note that the
	 * highest-used integer index is remembered separately, even if that index is removed
	 * from this array.
	 * 
	 * The new entry is appended at the end of the array.
	 * 
	 * @param value the value to append
	 */
	public void append(Object value) {
		putWithIntegerIndex(highestNumericIndexUsed + 1, value);
	}
	
	/**
	 * Gets the value for a key. This method returns null for missing keys.
	 * @param key the key
	 * @return the value
	 */
	public Object get(String key) {
		return keyValueMap.get(key);
	}
	
	/**
	 * Gets the value for a key. This method returns null for missing keys, but also
	 * reports them as warnings to the specified error reporter.
	 * 
	 * @param key the key
	 * @param errorReporter the error reporter used to report missing keys
	 * @return the value
	 */
	public Object get(String key, ErrorReporter errorReporter) {
		if (!keyValueMap.containsKey(key)) {
			errorReporter.reportWarning("undefined index: " + key);
		}
		return keyValueMap.get(key);
	}
	
	/**
	 * Puts a value into the array. TODO describe exact effect on key order
	 * @param key the key
	 * @param value the value
	 */
	public void put(String key, Object value) {
		if (key == null) {
			key = "";
		}
		try {
			putWithIntegerIndex(Integer.parseInt(key), value);
		} catch (NumberFormatException e) {
			putWithStringIndex(key, value);
		}
	}

	/**
	 * 
	 */
	private void putWithIntegerIndex(int index, Object value) {
		highestNumericIndexUsed = Math.max(highestNumericIndexUsed, index);
		// TODO
	}
	
	/**
	 * 
	 */
	private void putWithStringIndex(String index, Object value) {
		// TODO
	}
	
	/**
	 * Unsets an array entry.
	 * @param key the key to unset
	 */
	public void unset(String key) {
		keyValueMap.remove(key);
		keyList.remove(key);
	}
	
	/**
	 * Builds a list that contains the keys from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<String> getOrderedCopyOfKeys() {
		return new ArrayList<>(keyList);
	}
	
	/**
	 * Builds a list that contains the values from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<Object> getOrderedCopyOfValues() {
		final ArrayList<Object> result = new ArrayList<>();
		for (String key : keyList) {
			result.add(keyValueMap.get(key));
		}
		return result;
	}
	
	/**
	 * Builds a list that contains the entries from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<Pair<String, Object>> getOrderedCopyOfEntries() {
		final ArrayList<Pair<String, Object>> result = new ArrayList<>();
		for (String key : keyList) {
			result.add(Pair.of(key, keyValueMap.get(key)));
		}
		return result;
	}
	
	/**
	 * Returns an {@link Iterable} that represents a copy of the current keys.
	 * Being a copy, it is automatically protected against concurrent modification.
	 * 
	 * The copy is made each time an iterator is requested, not directly in this method.
	 * 
	 * @return the iterable
	 */
	public Iterable<String> getCopyingKeyIterable() {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return new ImmutableIteratorWrapper<>(getOrderedCopyOfKeys().iterator());
			}
		};
	}
	
	/**
	 * Returns an {@link Iterable} that represents a copy of the current values,
	 * in the same order as they are stored in this array. Being a copy, it is
	 * automatically protected against concurrent modification.
	 * 
	 * The copy is made each time an iterator is requested, not directly in this method.
	 * 
	 * @return the iterable
	 */
	public Iterable<Object> getCopyingValueIterable() {
		return new Iterable<Object>() {
			@Override
			public Iterator<Object> iterator() {
				return new ImmutableIteratorWrapper<>(getOrderedCopyOfValues().iterator());
			}
		};
	}
	
	/**
	 * Returns an {@link Iterable} that represents a copy of the current entries,
	 * in the same order as they are stored in this array. Being a copy, it is
	 * automatically protected against concurrent modification.
	 * 
	 * The copy is made each time an iterator is requested, not directly in this method.
	 * 
	 * @return the iterable
	 */
	public Iterable<Pair<String, Object>> getCopyingEntryIterable() {
		return new Iterable<Pair<String, Object>>() {
			@Override
			public Iterator<Pair<String, Object>> iterator() {
				return new ImmutableIteratorWrapper<>(getOrderedCopyOfEntries().iterator());
			}
		};
	}
	
}
