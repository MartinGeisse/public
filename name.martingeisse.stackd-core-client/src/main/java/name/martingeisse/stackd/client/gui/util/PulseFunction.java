/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.util;

/**
 * A function that returns a value in the range 0..255 for an input
 * time (measured in milliseconds). The functions should produce one
 * period per second.
 */
public enum PulseFunction {

	/**
	 * abs(sin(t))
	 */
	ABSOLUTE_SINE {
		@Override
		public int evaluate(int time) {
			double x = (time * 2 * Math.PI / 1000);
			double s = Math.abs(Math.sin(x));
			return (int)(s * 255);
		}
	};
	
	/**
	 * Evaluates this function.
	 * @param time the time
	 * @return the value (0..255) at that time
	 */
	public abstract int evaluate(int time);
	
	/**
	 * Evaluates this function.
	 * @param time the time
	 * @param period the pulse period
	 * @return the value (0..255) at that time
	 */
	public final int evaluate(int time, int period) {
		return evaluate(time * 1000 / period);
	}
	
}
