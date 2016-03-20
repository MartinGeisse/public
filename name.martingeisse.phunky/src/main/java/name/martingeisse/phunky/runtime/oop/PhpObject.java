/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.oop;

/**
 * Base class for PHP objects. Each PHP object is an instance of this class
 * in the Java sense of instance-of, and an instance of a {@link PhpClass}
 * in the PHP sense of instance-of. This base class exists to allow specialized
 * implementations for field access and method invocation for specialized
 * built-in {@link PhpClass}es.
 * 
 * The PHP instance-of relation is defined by {@link #getPhpClass()}.
 */
public abstract class PhpObject {

	/**
	 * Getter method for the PHP class of which this object is an instance.
	 * @return the PHP class
	 */
	public abstract PhpClass getPhpClass();
	
}
