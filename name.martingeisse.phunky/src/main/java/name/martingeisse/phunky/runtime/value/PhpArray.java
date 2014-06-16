/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.util.ImmutableIteratorWrapper;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.Variable;
import name.martingeisse.phunky.runtime.code.statement.Statement;

/**
 * PHP Array. This is similar to a {@link LinkedHashMap}, i.e. a map with an
 * additional imposed order, with a few extra features:
 * 
 * - string keys. PHP key behavior can be reduced to string-only keys, with
 *   a bit of special behavior for keys that can be parsed as integers.
 *   Specifically, 42 and "42" are considered to be the same key.
 * 
 * - each element is a {@link Variable} and can be the target of PHP references
 *   
 * - missing entries are different from entries whose value is null (the latter have
 *   a variable, while the former don't)
 * 
 * - remembers the highest-used numeric index to generate new indices
 *   when appending elements
 */
public final class PhpArray implements PhpIterable {

	/**
	 * the elements
	 */
	private final LinkedHashMap<String, Variable> elements = new LinkedHashMap<>();
	
	/**
	 * the highestNumericIndexUsed
	 */
	private int highestNumericIndexUsed = -1;
	
	/**
	 * Appends an element to this array, using the highest used integer index so far, plus
	 * one, as the key. Note that the highest-used integer index is remembered separately,
	 * even if that index is removed from this array.
	 * 
	 * The new entry is appended at the end of the array.
	 * 
	 * @return the variable for the new element
	 */
	public Variable append() {
		highestNumericIndexUsed++;
		Variable variable = new Variable();
		elements.put(Integer.toString(highestNumericIndexUsed), variable);
		return variable;
	}
	
	/**
	 * Inserts or replaces a variable, using the specified key.
	 * @param key the key
	 * @param variable the variable to set
	 */
	public void setVariable(String key, Variable variable) {
		elements.put(key, variable);
		try {
			highestNumericIndexUsed = Math.max(highestNumericIndexUsed, Integer.parseInt(key));
		} catch (NumberFormatException e) {
		}
	}
	
	/**
	 * Returns the variable for the specified key, or null TODO
	 * @param key
	 * @return
	 */
	public Variable getVariable(String key) {
		
	}
	
	/**
	 * Returns the variable for the specified key, creating it TODO 
	 * @param key
	 * @return
	 */
	public Variable getOrCreateVariable(String key) {
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Object put(String key, Object value) {
		Object result = super.put(key, value);
		return result;
	}
	
	/**
	 * Builds a list that contains the keys from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<String> getOrderedCopyOfKeys() {
		return new ArrayList<>(keySet());
	}
	
	/**
	 * Builds a list that contains the values from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<Object> getOrderedCopyOfValues() {
		return new ArrayList<>(values());
	}
	
	/**
	 * Builds a list that contains the entries from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<Map.Entry<String, Object>> getOrderedCopyOfEntries() {
		return new ArrayList<>(entrySet());
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
	public Iterable<Map.Entry<String, Object>> getCopyingEntryIterable() {
		return new Iterable<Map.Entry<String, Object>>() {
			@Override
			public Iterator<Map.Entry<String, Object>> iterator() {
				return new ImmutableIteratorWrapper<>(getOrderedCopyOfEntries().iterator());
			}
		};
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpIterable#iterate(name.martingeisse.phunky.runtime.Environment, java.lang.String, java.lang.String, name.martingeisse.phunky.runtime.code.statement.Statement)
	 */
	@Override
	public void iterate(Environment environment, String keyIterationVariableName, String valueIterationVariableName, Statement body) {
		if (keyIterationVariableName == null) {
			for (Object value : getCopyingValueIterable()) {
				environment.getOrCreate(valueIterationVariableName).setValue(value);
				body.execute(environment);
			}
		} else {
			for (Map.Entry<String, Object> entry : getCopyingEntryIterable()) {
				environment.getOrCreate(keyIterationVariableName).setValue(entry.getKey());
				environment.getOrCreate(valueIterationVariableName).setValue(entry.getValue());
				body.execute(environment);
			}
		}
	}
	
}
