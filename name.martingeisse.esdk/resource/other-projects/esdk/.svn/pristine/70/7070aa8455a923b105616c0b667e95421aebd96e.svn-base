/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.hdl.experiment;

import name.martingeisse.esdk.hdl.core.ClockSource;

/**
 * TODO: document me
 *
 */
public class SynchronousExperimentUtil {

	/**
	 * Simulates the specified number of clock cycles from the specified source.
	 * @param source the clock source
	 * @param count the number of clock cycles to simulate
	 */
	public static void simulateCycles(ClockSource source, int count) {
		for (int i=0; i<count; i++) {
			System.out.println("---------- clock cycle " + i + " ----------");
			source.performClockCycle();
		}
	}
}
