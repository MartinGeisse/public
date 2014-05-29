/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

/**
 * This exception is used to implement return statements.
 */
public class ReturnException extends RuntimeException {

	/**
	 * the returnValue
	 */
	private final Object returnValue;

	/**
	 * Constructor.
	 * @param returnValue the return value
	 */
	public ReturnException(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	/**
	 * Getter method for the returnValue.
	 * @return the returnValue
	 */
	public Object getReturnValue() {
		return returnValue;
	}
	
}
