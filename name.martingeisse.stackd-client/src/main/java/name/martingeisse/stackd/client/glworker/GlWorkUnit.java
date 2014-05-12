/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.glworker;

/**
 * Base class for all work units executed by the worker thread.
 */
public abstract class GlWorkUnit {

	/**
	 * Executes this work unit.
	 */
	public abstract void execute();
	
}
