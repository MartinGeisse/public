/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.rtl.experiment;

import name.martingeisse.esdk.rtl.ClockNetwork;

/**
 * Utility class that helps experimenting with HDL models.
 */
public class SynchronousExperimentUtil {

	/**
	 * Simulates the specified number of clock cycles from the specified source.
	 * @param clockNetwork the clock network
	 * @param count the number of clock cycles to simulate
	 */
	public static void simulateCycles(ClockNetwork clockNetwork, int count) {
		for (int i=0; i<count; i++) {
			System.out.println("---------- clock cycle " + i + " ----------");
			clockNetwork.performClockCycle();
		}
	}
}
