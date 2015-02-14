/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.terms;

/**
 * Denotes how many of a kind of object are allowed. This enumeration
 * distinguishes the fundamental amounts "zero", "one" and "more than one",
 * and specifies enumeration constants for all of the eight combinations
 * of those fundamental amounts, except:
 * 
 * - the empty combination that doesn't accept any number of objects as valid
 * - the "hole" combination that accepts zero or more than one, but not exactly one
 * - the combination that accepts only "more than one", i.e. at least two
 */
public enum Multiplicity {

	/**
	 * Accepts only exactly zero objects.
	 */
	ZERO,

	/**
	 * Accepts only exactly one object.
	 */
	ONE,

	/**
	 * Accepts zero or one object, i.e. denotes an optional object.
	 */
	ZERO_OR_ONE,
	
	/**
	 * Accepts more than zero objects, i.e. denotes a nonempty list.
	 */
	NONZERO,
	
	/**
	 * Accepts any number of objects, i.e. denotes a (possibly empty) list.
	 */
	ANY;
	
}
