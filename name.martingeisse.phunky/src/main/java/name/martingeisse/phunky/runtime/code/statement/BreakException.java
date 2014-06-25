/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

/**
 * This exception is used to implement break statements.
 */
public class BreakException extends RuntimeException {

	/**
	 * the remainingLoopsToBreak
	 */
	private int remainingLoopsToBreak;

	/**
	 * Constructor.
	 * @param remainingLoopsToBreak the remaining number of loops to break
	 */
	public BreakException(int remainingLoopsToBreak) {
		this.remainingLoopsToBreak = remainingLoopsToBreak;
	}

	/**
	 * Called when this exception breaks a loop.
	 * @return true to throw on, false to stop breaking
	 */
	public boolean onBreakLoop() {
		remainingLoopsToBreak--;
		return (remainingLoopsToBreak > 0);
	}
	
}
