/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.variable;

import java.util.LinkedHashMap;
import java.util.Map;

import name.martingeisse.phunky.runtime.PhpRuntime;

/**
 * PHP Array. This is similar to a {@link LinkedHashMap}, i.e. a map with an
 * additional imposed order, with a few extra features:
 * 
 * - string keys. PHP key behavior can be reduced to string-only keys, with
 *   a bit of special behavior for keys that can be parsed as integers.
 *   Specifically, 42 and "42" are considered to be the same key.
 *   
 * - missing entries are different from entries whose value is null
 * 
 * - arrays come in two flavors: Variable arrays and value arrays. Variable
 *   arrays are stored in variables and contain variables. They and their
 *   elements can be the target of references or contain references. Value
 *   arrays store values only, not variables, and are used for values being
 *   passed around. A variable can contain a value array as long as the array
 *   isn't modified; any such modification must first clone the array as a 
 *   variable array.
 *   
 * This base class provides transient read access to an array's keys and values.
 * A reference to a {@link PhpArray} should not be stored. Instead, this
 * interface should be used to read data form an array during a short-running
 * operation that is not interleaved with user-provided code, such as a
 * built-in function.
 * 
 * If lasting access is needed either as a stored value or as a referenced
 * variable, then this interface provides methods for that.
 * 
 * No write access is granted since the data might be shared transparently.
 * No references to elements are possible, for the same reason.
 * No guarantees are made about whether the array is immutable -- it might
 * be a writable array that is later modified from elsewhere.
 * If the array is writable, then no guarantees are made about whether write
 * operations from elsewhere are actually visible through this interface.
 */
public abstract class PhpArray {
	
	/**
	 * Checks whether this array is empty.
	 * @return true if empty, false if not
	 */
	public abstract boolean isEmpty();

	/**
	 * Returns the value for the specified key, or null if the key doesn't exist.
	 * 
	 * The value may itself be a {@link PhpArray} making no more guarantees than
	 * this object itself.
	 * 
	 * @param key the key
	 * @return the value
	 */
	public abstract Object getValue(final String key);
	
	/**
	 * Returns the value for the specified key, or null if the key doesn't exist.
	 * In the latter case this method also triggers an error. Note that this is
	 * different from an existing key whose value is null.
	 * 
	 * The value may itself be a {@link PhpArray} making no more guarantees than
	 * this object itself.
	 * 
	 * @param runtime the runtime used to trigger an error
	 * @param key the key
	 * @return the value
	 */
	public abstract Object getValueOrError(final PhpRuntime runtime, final String key);
	
	/**
	 * Returns an {@link Iterable} that directly iterates over the current keys.
	 * 
	 * Even if this array is known to be a variable array and thus mutable,
	 * neither the array structure nor the keys may be modified while iterating;
	 * however, for variable arrays, the values contained in the array *may* be
	 * modified.
	 * 
	 * @return the iterable
	 */
	public abstract Iterable<String> getKeyIterable();

	/**
	 * Returns an {@link Iterable} that directly iterates over the current values list.
	 * 
	 * Even if this array is known to be a variable array and thus mutable,
	 * neither the array structure nor the keys may be modified while iterating;
	 * however, for variable arrays, the values contained in the array *may* be
	 * modified. Such modifications may or may not be visible through the iterator.
	 * 
	 * @return the iterable
	 */
	public abstract Iterable<Object> getValueIterable();

	/**
	 * Returns an {@link Iterable} that directly iterates over the current entries.
	 * 
	 * Even if this array is known to be a variable array and thus mutable,
	 * neither the array structure nor the keys may be modified while iterating;
	 * however, for variable arrays, the values contained in the array *may* be
	 * modified. Such modifications may or may not be visible through the iterator.
	 * 
	 * @return the iterable
	 */
	public abstract Iterable<Map.Entry<String, Object>> getKeyValueEntryIterable();
	
	/**
	 * If this array is a variable array, this method just returns this array.
	 * Otherwise it builds a new variable array with the same contents.
	 * 
	 * @return the variable array
	 */
	public abstract PhpVariableArray toVariableArray();
	
	/**
	 * If this array is a value array, this method just returns this array.
	 * Otherwise it builds a new value array with the same contents.
	 * 
	 * @return the value array
	 */
	public abstract PhpValueArray toValueArray();

}
