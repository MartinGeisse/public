/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Note: This class isn't 100% safe against race conditions, because it would miss
 * timer events anyway when the game is under load. It should, however, be
 * safe against crashing altogether, i.e. the worst thing that could happen are
 * missed events.
 */
public final class FrameTimer extends Timer {

	/**
	 * the fired
	 */
	private volatile boolean fired;

	/**
	 * Constructor.
	 * @param interval the interval of the timer (in milliseconds) 
	 */
	public FrameTimer(long interval) {
		fired = false;
		schedule(new MyTask(), 0, interval);
	}

	/**
	 * Tests whether the timer has fired, and resets it if so.
	 * @return true if the timer has fired, false if not
	 */
	public boolean test() {
		boolean result = fired;
		fired = false;
		return result;
	}
	
	/**
	 *
	 */
	private class MyTask extends TimerTask {

		/* (non-Javadoc)
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
			fired = true;
			synchronized(FrameTimer.this) {
				FrameTimer.this.notify();
			}
		}

	}
	
}
