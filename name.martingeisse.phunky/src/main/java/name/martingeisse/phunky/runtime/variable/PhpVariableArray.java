/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.variable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import name.martingeisse.common.util.iterator.AbstractIterableWrapper;
import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.statement.Statement;

import org.apache.commons.lang3.tuple.Pair;

/**
 * A variable array, i.e. a hash table containing key/variable mappings.
 * This object can only be used as the value of a variable, not as a
 * value being passed around.
 * 
 * Each element is a {@link Variable} and can be the target of PHP references.
 * 
 * Missing entries are distinguished from null entries in that the latter have
 * a variable with a null value, while the former don't have a variable at all.
 * 
 * This class remembers the highest-used numeric index to generate new indices
 * when appending elements.
 */
public final class PhpVariableArray extends PhpArray implements MutableVariableContent {

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
	 * Any {@link MutableVariableContent} from the iterator will be converted to an immutable value first.
	 * 
	 * @param values the values
	 * @return the array
	 */
	public static PhpVariableArray fromValues(Iterable<?> values) {
		final PhpVariableArray array = new PhpVariableArray();
		for (final Object value : values) {
			// values can be stored directly in variables
			array.append().setValue(TypeConversionUtil.makeImmutable(value));
		}
		return array;
	}

	/**
	 * Returns a new array containing the specified keys and values, in the same
	 * order as returned by the iterator.
	 * 
	 * Any {@link MutableVariableContent} from the iterator will be converted to an immutable value first.
	 * 
	 * @param keyValueEntries the key/value entries
	 * @return the array
	 */
	public static PhpVariableArray fromKeyValueEntries(Iterable<Map.Entry<String, Object>> keyValueEntries) {
		PhpVariableArray array = new PhpVariableArray();
		for (Map.Entry<String, Object> entry : keyValueEntries) {
			array.setVariable(entry.getKey(), new Variable(TypeConversionUtil.makeImmutable(entry.getValue())));
		}
		return array;
	}
	
	/**
	 * Returns a new array containing the specified keys and values, in the same
	 * order as returned by the iterator.
	 * 
	 * Any {@link MutableVariableContent} from the iterator will be converted to an immutable value first.
	 * 
	 * @param keyValuePairs the key/value pair
	 * @return the array
	 */
	public static PhpVariableArray fromKeyValuePairs(Iterable<Pair<String, Object>> keyValuePairs) {
		PhpVariableArray array = new PhpVariableArray();
		for (Pair<String, Object> pair : keyValuePairs) {
			array.setVariable(pair.getKey(), new Variable(TypeConversionUtil.makeImmutable(pair.getValue())));
		}
		return array;
	}

	/**
	 * Returns a new array containing the specified keys and values from variables, in the same
	 * order as returned by the iterator.
	 * 
	 * Any {@link MutableVariableContent} from the iterated variables will be converted to an immutable value first.
	 * 
	 * @param keyVariableEntries the key/variable entries
	 * @return the array
	 */
	public static PhpVariableArray fromCopiedKeyVariableEntries(Iterable<Map.Entry<String, Variable>> keyVariableEntries) {
		PhpVariableArray array = new PhpVariableArray();
		for (Map.Entry<String, Variable> entry : keyVariableEntries) {
			// request a value from the original variable (making a copy of any MutableVariableContent) and store it in a new variable
			array.setVariable(entry.getKey(), new Variable(entry.getValue().getValue()));
		}
		return array;
	}
	
	/**
	 * Returns a new array containing the specified keys and values from variables, in the same
	 * order as returned by the iterator.
	 * 
	 * Any {@link MutableVariableContent} from the iterated variables will be converted to an immutable value first.
	 * 
	 * @param keyVariablePairs the key/variable pair
	 * @return the array
	 */
	public static PhpVariableArray fromCopiedKeyVariablePairs(Iterable<Pair<String, Variable>> keyVariablePairs) {
		PhpVariableArray array = new PhpVariableArray();
		for (Pair<String, Variable> pair : keyVariablePairs) {
			// request a value from the original variable (making a copy of any MutableVariableContent) and store it in a new variable
			array.setVariable(pair.getKey(), new Variable(pair.getValue().getValue()));
		}
		return array;
	}
	
	/**
	 * Returns a new array containing the specified keys and variables, in the same order as
	 * returned by the iterator. The variables returned by the iterator are shared by the
	 * newly created array.
	 * 
	 * @param keyVariableEntries the key/variable entries
	 * @return the array
	 */
	public static PhpVariableArray fromSharedKeyVariableEntries(Iterable<Map.Entry<String, Variable>> keyVariableEntries) {
		PhpVariableArray array = new PhpVariableArray();
		for (Map.Entry<String, Variable> entry : keyVariableEntries) {
			// re-use the original variable
			array.setVariable(entry.getKey(), entry.getValue());
		}
		return array;
	}
	
	/**
	 * Returns a new array containing the specified keys and variables, in the same order as
	 * returned by the iterator. The variables returned by the iterator are shared by the
	 * newly created array.
	 * 
	 * @param keyVariablePairs the key/variable pair
	 * @return the array
	 */
	public static PhpVariableArray fromSharedKeyVariablePairs(Iterable<Pair<String, Variable>> keyVariablePairs) {
		PhpVariableArray array = new PhpVariableArray();
		for (Pair<String, Variable> pair : keyVariablePairs) {
			// re-use the original variable
			array.setVariable(pair.getKey(), pair.getValue());
		}
		return array;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#isEmpty()
	 */
	@Override
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
			setVariable(key, variable);
		}
		return variable;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String key) {
		Variable variable = getVariable(key);
		if (variable == null) {
			return null;
		} else {
			return variable.getValue();
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getValueOrError(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.String)
	 */
	@Override
	public Object getValueOrError(PhpRuntime runtime, String key) {
		Variable variable = getVariable(key);
		if (variable == null) {
			runtime.triggerError("undefined index: " + key);
			return null;
		} else {
			return variable.getValue();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getKeyIterable()
	 */
	@Override
	public Iterable<String> getKeyIterable() {
		return elements.keySet();
	}

	/**
	 * Returns an {@link Iterable} that iterates over the current variable list.
	 * 
	 * Neither the array structure nor the keys may be modified while iterating;
	 * however, the values contained in the variables *may* be modified. Such
	 * modifications may or may not be visible through the iterator.
	 * 
	 * @return the iterable
	 */
	public Iterable<Variable> getVariableIterable() {
		return elements.values();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getValueIterable()
	 */
	@Override
	public Iterable<Object> getValueIterable() {
		return new AbstractIterableWrapper<Variable, Object>(elements.values()) {
			@Override
			protected Object handleElement(final Variable variable) {
				return variable.getValue();
			}
		};
	}

	/**
	 * Returns an {@link Iterable} that iterates over the current key/variable entries.
	 * 
	 * Neither the array structure nor the keys may be modified while iterating;
	 * however, the values contained in the variables *may* be modified. Such
	 * modifications may or may not be visible through the iterator.
	 * 
	 * @return the iterable
	 */
	public Iterable<Map.Entry<String, Variable>> getKeyVariableIterable() {
		return elements.entrySet();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getKeyValueEntryIterable()
	 */
	@Override
	public Iterable<Entry<String, Object>> getKeyValueEntryIterable() {
		return new AbstractIterableWrapper<Map.Entry<String, Variable>, Map.Entry<String, Object>>(elements.entrySet()) {
			@Override
			protected Entry<String, Object> handleElement(final Entry<String, Variable> element) {
				return new Map.Entry<String, Object>() {

					/* (non-Javadoc)
					 * @see java.util.Map.Entry#getKey()
					 */
					@Override
					public String getKey() {
						return element.getKey();
					}

					/* (non-Javadoc)
					 * @see java.util.Map.Entry#getValue()
					 */
					@Override
					public Object getValue() {
						return element.getValue().getValue();
					}

					/* (non-Javadoc)
					 * @see java.util.Map.Entry#setValue(java.lang.Object)
					 */
					@Override
					public Object setValue(Object value) {
						Object oldValue = element.getValue().getValue();
						element.getValue().setValue(TypeConversionUtil.makeImmutable(value));
						return oldValue;
					}
					
				};
			}
		};
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#toVariableArray()
	 */
	@Override
	public PhpVariableArray toVariableArray() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#toValueArray()
	 */
	@Override
	public PhpValueArray toValueArray() {
		return PhpValueArray.fromKeyVariableEntries(elements.entrySet());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.MutableVariableContent#createImmutableCopy()
	 */
	@Override
	public Object createImmutableCopy() {
		return toValueArray();
	}

	/**
	 * Iterates over the elements of this object. For each element, the key iteration variable is bound
	 * by-value and the value iteration variable is bound by-reference. Then the body is executed. 
	 * 
	 * @param environment the environment
	 * @param keyIterationVariableName the name of the key iteration variable, or null if none
	 * @param valueIterationVariableName the name of the value iteration variable, or null if none
	 * @param body the body to execute
	 */
	public void iterateWithValueReference(final Environment environment, final String keyIterationVariableName, final String valueIterationVariableName, final Statement body) {
		for (final Map.Entry<String, Variable> entry : elements.entrySet()) {
			if (keyIterationVariableName != null) {
				environment.getOrCreate(keyIterationVariableName).setValue(entry.getKey());
			}
			if (valueIterationVariableName != null) {
				environment.put(valueIterationVariableName, entry.getValue());
			}
			body.execute(environment);
		}
	}

}
