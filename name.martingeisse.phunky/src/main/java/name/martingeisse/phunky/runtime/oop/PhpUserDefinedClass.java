/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.oop;

import org.apache.commons.lang3.NotImplementedException;

/**
 * The concrete metaclass implementation for classes that have been
 * defined in PHP code (as opposed to built-in classes).
 */
public final class PhpUserDefinedClass extends PhpClass {

	/**
	 * Constructor.
	 */
	public PhpUserDefinedClass() {
		throw new NotImplementedException("user-defined classes not implemented yet");
	}
	
}
