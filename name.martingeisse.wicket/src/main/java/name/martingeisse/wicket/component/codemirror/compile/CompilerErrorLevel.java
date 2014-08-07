/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror.compile;

/**
 * Used to indicate the severity of an error.
 */
public enum CompilerErrorLevel {

	/**
	 * Indicates an error.
	 */
	ERROR,
	
	/**
	 * Indicates something that is wrong but not formally an error.
	 */
	WARNING,
	
	/**
	 * Indicates an informational message that does not indicate an error.
	 */
	INFO;
	
}
