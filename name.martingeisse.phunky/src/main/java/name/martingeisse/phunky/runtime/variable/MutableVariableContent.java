/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.variable;

/**
 * Objects which implement this interface can be stored
 * in variables but should not be passed around as values since no
 * semantics are defined for when the value is copied and when it is
 * shared. The only "proper" way to use objects implementing this
 * interface is to store them in a {@link Variable} and perform
 * operations on it while it is stored there.
 * 
 * Currently the only class which implements this interface is
 * {@link PhpVariableArray}. It does so because it represents a
 * mutable array which contains variables, not just values. Using such
 * an array as a value requires making an immutable copy of it.
 * 
 * Though only variable arrays are mutable at the moment, other mutable
 * variable content objects would be possible as long as the class
 * {@link Variable} contains specialized accessor methods for them.
 */
public interface MutableVariableContent {

	/**
	 * Creates a copy of this object that is an immutable value but
	 * represents the same data. The copy must be an instance of another
	 * class so it doesn't implement {@link MutableVariableContent}.
	 * 
	 * For {@link PhpVariableArray}, this method returns a
	 * corresponding {@link PhpValueArray}.
	 * 
	 * Implementations are allowed to return the same object in subsequent
	 * calls (assuming no modification of this mutable object in between)
	 * since conceptually-immutable value objects can be transparently
	 * shared.
	 * 
	 * @return the immutable copy
	 */
	public Object createImmutableCopy();
	
}
