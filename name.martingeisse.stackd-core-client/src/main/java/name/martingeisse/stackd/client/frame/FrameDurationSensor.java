/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame;

/**
 * Returns the duration of the previous frame to make game logic adapt
 * to a varying frame rate.
 * 
 * The sensor is capped to a maximum of 1/2 second to avoid "exploding"
 * the game logic.
 */
public final class FrameDurationSensor {

	/**
	 * the previousValue
	 */
	private long previousValue;

	/**
	 * the currentValue
	 */
	private long currentValue;

	/**
	 * Constructor.
	 */
	public FrameDurationSensor() {
		tick();
		tick();
	}
	
	/**
	 * This method must be called for each frame, before reading the duration.
	 */
	public void tick() {
		previousValue = currentValue;
		currentValue = System.nanoTime();
	}
	
	/**
	 * Reads the frame duration sensor as a double-typed multiplier, with
	 * 1.0 meaning one second.
	 * @return the duration of the previous frame in seconds
	 */
	public double getMultiplier() {
		double value = getNanoseconds();
		return value / 1000000000.0;
	}
	
	/**
	 * Reads the frame duration sensor with millisecond precision.
	 * @return the duration of the previous frame in milliseconds
	 */
	public long getMilliseconds() {
		return getNanoseconds() / 1000000;
	}
	
	/**
	 * Reads the frame duration sensor with microsecond precision.
	 * @return the duration of the previous frame in microseconds
	 */
	public long getMicroseconds() {
		return getNanoseconds() / 1000;
	}
	
	/**
	 * Reads the frame duration sensor with nanosecond precision.
	 * @return the duration of the previous frame in nanoseconds
	 */
	public long getNanoseconds() {
		long value = (currentValue - previousValue);
		if (value > 1000000000) {
			value = 1000000000;
		}
		return value;
	}
	
}
