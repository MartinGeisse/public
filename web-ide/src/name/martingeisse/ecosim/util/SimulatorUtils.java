/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

import name.martingeisse.ecosim.timer.ITickable;

/**
 * Common static methods for the simulator.
 */
public class SimulatorUtils {

	/**
	 * Sends multiple ticks to the specified tickable.
	 * @param t the tickable
	 * @param n the number of ticks to send
	 */
	public static void multiTick(ITickable t, int n) {
		while (n > 0) {
			t.tick();
			n--;
		}
	}
	
}
