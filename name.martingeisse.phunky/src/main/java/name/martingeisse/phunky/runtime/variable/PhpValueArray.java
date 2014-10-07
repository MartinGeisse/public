/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.variable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.statement.Statement;

import org.apache.commons.lang3.tuple.Pair;

/**
 * A value array, i.e. a hash table containing key/value mappings.
 * This object can be used as a value being passed around, including as the
 * value of a variable. However, it cannot be modified. To modify or reference
 * the array or one of its elements, it must first be cloned as a variable
 * array.
 * 
 * Missing entries are distinguished from null entries in that the latter have
 * an entry in the hash table.
 */
public final class PhpValueArray extends PhpArray implements PhpValueIterationProvider {

	/**
	 * the elements
	 */
	private final LinkedHashMap<String, Object> elements = new LinkedHashMap<>();

	/**
	 * Returns a new array containing the specified values, in the same order as returned
	 * by the iterator.
	 * 
	 * Any {@link MutableVariableContent} from the iterator will be converted to an immutable value first.
	 * 
	 * @param values the values
	 * @return the array
	 */
	public static PhpValueArray fromValues(Iterable<?> values) {
		PhpValueArray array = new PhpValueArray();
		int i=0;
		for (Object value : values) {
			array.elements.put(Integer.toString(i), TypeConversionUtil.makeImmutable(value));
			i++;
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
	public static PhpValueArray fromKeyValueEntries(Iterable<Map.Entry<String, Object>> keyValueEntries) {
		PhpValueArray array = new PhpValueArray();
		for (Map.Entry<String, Object> entry : keyValueEntries) {
			array.elements.put(entry.getKey(), TypeConversionUtil.makeImmutable(entry.getValue()));
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
	public static PhpValueArray fromKeyValuePairs(Iterable<Pair<String, Object>> keyValuePairs) {
		PhpValueArray array = new PhpValueArray();
		for (Pair<String, Object> pair : keyValuePairs) {
			array.elements.put(pair.getKey(), TypeConversionUtil.makeImmutable(pair.getValue()));
		}
		return array;
	}

	/**
	 * Returns a new array containing the specified keys and values from the variables,
	 * in the same order as returned by the iterator.
	 * 
	 * Any {@link MutableVariableContent} from the iterated variables will be converted to an immutable value first.
	 * 
	 * @param keyVariableEntries the key/variable entries
	 * @return the array
	 */
	public static PhpValueArray fromKeyVariableEntries(Iterable<Map.Entry<String, Variable>> keyVariableEntries) {
		PhpValueArray array = new PhpValueArray();
		for (Map.Entry<String, Variable> entry : keyVariableEntries) {
			// if the variable's value is a MutableVariableContent, then this makes a copy
			array.elements.put(entry.getKey(), entry.getValue().getValue());
		}
		return array;
	}
	
	/**
	 * Returns a new array containing the specified keys and variables, in the same
	 * order as returned by the iterator.
	 * 
	 * Any {@link MutableVariableContent} from the iterated variables will be converted to an immutable value first.
	 * 
	 * @param keyVariablePairs the key/variable pair
	 * @return the array
	 */
	public static PhpValueArray fromKeyVariablePairs(Iterable<Pair<String, Variable>> keyVariablePairs) {
		PhpValueArray array = new PhpValueArray();
		for (Pair<String, Variable> pair : keyVariablePairs) {
			// if the variable's value is a MutableVariableContent, then this makes a copy
			array.elements.put(pair.getKey(), pair.getValue().getValue());
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
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.PhpArray#size()
	 */
	@Override
	public int size() {
		return elements.size();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String key) {
		return elements.get(key);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getValueOrError(name.martingeisse.phunky.runtime.PhpRuntime, java.lang.String)
	 */
	@Override
	public Object getValueOrError(PhpRuntime runtime, String key) {
		Object value = elements.get(key);
		if (value == null && !elements.containsKey(key)) {
			runtime.triggerError("undefined index: " + key);
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getKeyIterable()
	 */
	@Override
	public Iterable<String> getKeyIterable() {
		return elements.keySet();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getValueIterable()
	 */
	@Override
	public Iterable<Object> getValueIterable() {
		return elements.values();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#getKeyValueEntryIterable()
	 */
	@Override
	public Iterable<Entry<String, Object>> getKeyValueEntryIterable() {
		return elements.entrySet();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#toVariableArray()
	 */
	@Override
	public PhpVariableArray toVariableArray() {
		return PhpVariableArray.fromKeyValueEntries(elements.entrySet());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.value.PhpArray#toValueArray()
	 */
	@Override
	public PhpValueArray toValueArray() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.variable.PhpValueIterationProvider#iterate(name.martingeisse.phunky.runtime.Environment, java.lang.String, java.lang.String, name.martingeisse.phunky.runtime.code.statement.Statement)
	 */
	@Override
	public void iterate(final Environment environment, final String keyIterationVariableName, final String valueIterationVariableName, final Statement body) {
		for (final Map.Entry<String, Object> entry : elements.entrySet()) {
			if (keyIterationVariableName != null) {
				environment.getOrCreate(keyIterationVariableName).setValue(entry.getKey());
			}
			if (valueIterationVariableName != null) {
				environment.getOrCreate(valueIterationVariableName).setValue(entry.getValue());
			}
			body.execute(environment);
		}
	}

}
