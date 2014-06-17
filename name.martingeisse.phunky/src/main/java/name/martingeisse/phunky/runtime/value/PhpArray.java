/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.value;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import name.martingeisse.common.util.ImmutableIteratorWrapper;
import name.martingeisse.common.util.iterator.AbstractIterableWrapper;
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
	 * Returns a new array containing the specified values, in the same order as returned
	 * by the iterator.
	 * 
	 * @param values the values
	 * @return the array
	 */
	public static PhpArray fromValues(Iterable<?> values) {
		PhpArray array = new PhpArray();
		for (Object value : values) {
			array.append().setValue(value);
		}
		return array;
	}
	
	/**
	 * Checks whether this array is empty.
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return elements.isEmpty();
	}

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
		final Variable variable = new Variable();
		elements.put(Integer.toString(highestNumericIndexUsed), variable);
		return variable;
	}

	/**
	 * Inserts or replaces a variable, using the specified key.
	 * @param key the key
	 * @param variable the variable to set
	 */
	public void setVariable(final String key, final Variable variable) {
		elements.put(key, variable);
		try {
			highestNumericIndexUsed = Math.max(highestNumericIndexUsed, Integer.parseInt(key));
		} catch (final NumberFormatException e) {
		}
	}

	/**
	 * Returns the variable for the specified key, or null if the key doesn't exist.
	 * @param key the key
	 * @return the variable
	 */
	public Variable getVariable(final String key) {
		return elements.get(key);
	}

	/**
	 * Returns the variable for the specified key, creating it if it doesn't exist 
	 * @param key the key
	 * @return the variable
	 */
	public Variable getOrCreateVariable(final String key) {
		Variable variable = elements.get(key);
		if (variable == null) {
			variable = new Variable();
			elements.put(key, variable);
		}
		return variable;
	}

	/**
	 * Builds a list that contains the keys from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<String> getOrderedCopyOfKeys() {
		return new ArrayList<>(elements.keySet());
	}

	/**
	 * Builds a list that contains the variables from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<Variable> getOrderedCopyOfVariables() {
		return new ArrayList<>(elements.values());
	}

	/**
	 * Builds a list that contains the values from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<Object> getOrderedCopyOfValues() {
		final List<Object> result = new ArrayList<>(elements.size());
		for (final Variable variable : elements.values()) {
			result.add(variable.getValue());
		}
		return result;
	}

	/**
	 * Builds a list that contains the entries from this array in the order they are stored.
	 * @return the newly created list
	 */
	public List<Map.Entry<String, Variable>> getOrderedCopyOfEntries() {
		return new ArrayList<>(elements.entrySet());
	}

	/**
	 * Returns an {@link Iterable} that represents a copy of the current keys.
	 * Being a copy, it is automatically protected against concurrent modification
	 * of the list structure.
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
	 * Returns an {@link Iterable} that represents a copy of the current variable list,
	 * but using the original {@link Variable} objects, in the same order as they are
	 * stored in this array. Being a copy, it is automatically protected against
	 * concurrent modification of the list structure.
	 * 
	 * The copy is made each time an iterator is requested, not directly in this method.
	 * 
	 * @return the iterable
	 */
	public Iterable<Variable> getCopyingVariableIterable() {
		return new Iterable<Variable>() {
			@Override
			public Iterator<Variable> iterator() {
				return new ImmutableIteratorWrapper<>(getOrderedCopyOfVariables().iterator());
			}
		};
	}

	/**
	 * Returns an {@link Iterable} that represents a copy of the current values,
	 * in the same order as they are stored in this array. Being a copy, it is
	 * automatically protected against concurrent modification of the list structure.
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
	 * automatically protected against concurrent modification of the list structure.
	 * 
	 * The copy is made each time an iterator is requested, not directly in this method.
	 * 
	 * @return the iterable
	 */
	public Iterable<Map.Entry<String, Variable>> getCopyingEntryIterable() {
		return new Iterable<Map.Entry<String, Variable>>() {
			@Override
			public Iterator<Map.Entry<String, Variable>> iterator() {
				return new ImmutableIteratorWrapper<>(getOrderedCopyOfEntries().iterator());
			}
		};
	}

	/**
	 * Returns an {@link Iterable} that directly iterates over the current keys. No copy
	 * is made. If the array is modified while iterating, the iterator throws a
	 * {@link ConcurrentModificationException}.
	 * 
	 * @return the iterable
	 */
	public Iterable<String> getDirectKeyIterable() {
		return elements.keySet();
	}

	/**
	 * Returns an {@link Iterable} that directly iterates over the current variable list.
	 * No copy is made. If the array is modified while iterating, the iterator throws a
	 * {@link ConcurrentModificationException}.
	 * 
	 * @return the iterable
	 */
	public Iterable<Variable> getDirectVariableIterable() {
		return elements.values();
	}

	/**
	 * Returns an {@link Iterable} that directly iterates over the current values list.
	 * No copy is made. If the array is modified while iterating, the iterator throws a
	 * {@link ConcurrentModificationException}.
	 * 
	 * @return the iterable
	 */
	public Iterable<Object> getDirectValueIterable() {
		return new AbstractIterableWrapper<Variable, Object>(elements.values()) {
			@Override
			protected Object handleElement(final Variable variable) {
				return variable.getValue();
			}
		};
	}

	/**
	 * Returns an {@link Iterable} that directly iterates over the current entries.
	 * No copy is made. If the array is modified while iterating, the iterator throws a
	 * {@link ConcurrentModificationException}.
	 * 
	 * @return the iterable
	 */
	public Iterable<Map.Entry<String, Variable>> getDirectEntryIterable() {
		return elements.entrySet();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpIterable#iterate(name.martingeisse.phunky.runtime.Environment, java.lang.String, java.lang.String, name.martingeisse.phunky.runtime.code.statement.Statement)
	 */
	@Override
	public void iterate(final Environment environment, final String keyIterationVariableName, final String valueIterationVariableName, final Statement body) {
		if (keyIterationVariableName == null) {
			for (final Object value : getCopyingValueIterable()) {
				environment.getOrCreate(valueIterationVariableName).setValue(value);
				body.execute(environment);
			}
		} else {
			for (final Map.Entry<String, Variable> entry : getCopyingEntryIterable()) {
				environment.getOrCreate(keyIterationVariableName).setValue(entry.getKey());
				environment.getOrCreate(valueIterationVariableName).setValue(entry.getValue());
				body.execute(environment);
			}
		}
	}

}
