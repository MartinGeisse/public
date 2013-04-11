/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.timer;

/**
 * This interface is used by objects that can receive ticks
 * from the simulation clock.
 */
public interface ITickable {

	/**
	 * Sends a clock tick to this object.
	 */
	public void tick();
	
}
