/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame;

/**
 * Base class for frame handlers that perform a specific action
 * in regular intervals, rounded up to full frames. The interval
 * is specified in milliseconds since a more precise unit would
 * not be useful when rounded to the length of a frame.
 * 
 * This class is coded in such a way that "long" frames, lasting
 * long enough to see the interval timer expire twice or more,
 * still fire the handling method only once. It is also coded
 * in such a way that the timer restarts at the time the handling
 * method runs, and not at the time it should have run (in case
 * the previous frame lasted too long). This makes this timer
 * useful for idempotent update tasks, and less useful for tasks
 * that must obey a fixed frequency.
 */
public abstract class AbstractIntervalFrameHandler extends AbstractFrameHandler {

	/**
	 * the intervalMilliseconds
	 */
	private final long intervalMilliseconds;
	
	/**
	 * the lastFiredTime
	 */
	private long lastFiredTime;
	
	/**
	 * Constructor.
	 * @param intervalMilliseconds the length of the interval in milliseconds
	 */
	public AbstractIntervalFrameHandler(long intervalMilliseconds) {
		this.intervalMilliseconds = intervalMilliseconds;
		this.lastFiredTime = System.currentTimeMillis();
	}
	
	/**
	 * Getter method for the intervalMilliseconds.
	 * @return the intervalMilliseconds
	 */
	public long getIntervalMilliseconds() {
		return intervalMilliseconds;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		super.handleStep();
		long currentTime = System.currentTimeMillis();
		if (currentTime > lastFiredTime + intervalMilliseconds) {
			onIntervalTimerExpired();
			lastFiredTime = currentTime;
		}
	}
	
	/**
	 * This method is invoked whenever the interval timer is expired.
	 */
	protected abstract void onIntervalTimerExpired();
	
}
