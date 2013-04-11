/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.timer;

/**
 * This is an abstract timer that is useful for many simulation purposes.
 * It invokes a callback method (supplied by the concrete implementation
 * class) every N ticks, where N is the interval length and can be set.
 * 
 * Formally, the timer keeps two properties, the interval length and the
 * number of ticks left. Sending a tick to the timer decreases the
 * number of ticks left, and, if that makes it zero, invokes the
 * onExpire() method and resets the number of ticks left to the interval
 * length. Both the interval length and the number of ticks left are stored
 * as 32-bit integers (it does not make a difference whether they are
 * regarded signed or unsigned).
 * 
 * Setting the interval length does not affect the number of ticks left in
 * the current interval, and will take effect with the start of the next
 * interval (unless the number of ticks left is modified by other means).
 * 
 * Setting the number of ticks left affects only the remaining length
 * of the current interval. This remaining length can be set beyond the
 * specified interval length.
 * 
 * The initial number of ticks left is equal to the initial interval
 * length, which is specified in the constructor.
 * 
 * The following special cases can be derived from the above specification.
 * Setting an interval length of 1 makes the timer expire every tick.
 * Setting an interval length of 2 makes the timer expire every other tick.
 * Setting an interval length of 0 makes the timer expire every 2^32 ticks.
 */
public abstract class AbstractIntervalTimer implements ITickable {

	/**
	 * the interval
	 */
	private int interval;

	/**
	 * the ticksLeft
	 */
	private int ticksLeft;

	/**
	 * Constructor
	 * @param interval the initial interval length
	 */
	public AbstractIntervalTimer(int interval) {
		this.interval = interval;
		this.ticksLeft = interval;
	}

	/**
	 * @return Returns the interval.
	 */
	public final int getInterval() {
		return interval;
	}

	/**
	 * Sets the interval.
	 * @param interval the new value to set
	 */
	public final void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * @return Returns the ticksLeft.
	 */
	public final int getTicksLeft() {
		return ticksLeft;
	}

	/**
	 * Sets the ticksLeft.
	 * @param ticksLeft the new value to set
	 */
	public final void setTicksLeft(int ticksLeft) {
		this.ticksLeft = ticksLeft;
	}
	
	/**
	 * Resets the timer to the full interval. This is a shortcut
	 * for setTicksLeft(getInterval()).
	 */
	public final void reset() {
		ticksLeft = interval;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public final void tick() {
		ticksLeft--;
		if (ticksLeft == 0) {
			ticksLeft = interval;
			onExpire();
		}
	}
	
	/**
	 * This method is invoked when the timer expires.
	 */
	protected abstract void onExpire();

}
