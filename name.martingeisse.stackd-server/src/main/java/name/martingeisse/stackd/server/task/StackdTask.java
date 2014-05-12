/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.stackd.server.task;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Base class for tasks that can be scheduled.
 */
public abstract class StackdTask implements Runnable {

	/**
	 * the executorService
	 */
	private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(3);
	
	/**
	 * Schedules this task to run ASAP.
	 */
	public final void schedule() {
		executorService.execute(this);
	}
	
	/**
	 * Schedules this task to run N milliseconds in the future.
	 * 
	 * @param milliseconds the number of milliseconds to run in the future
	 */
	public final void scheduleRelative(long milliseconds) {
		executorService.schedule(this, milliseconds, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules this task to run in N milliseconds.
	 * 
	 * @param delay the delay to wait before execution
	 * @param timeUnit the unit used for the delay
	 */
	public final void scheduleRelative(long delay, TimeUnit timeUnit) {
		executorService.schedule(this, delay, timeUnit);
	}
	
}
