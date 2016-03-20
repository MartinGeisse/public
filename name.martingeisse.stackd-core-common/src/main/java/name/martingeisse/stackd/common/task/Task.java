/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.task;

import java.util.concurrent.TimeUnit;

/**
 * Base class for tasks that can be scheduled.
 */
public abstract class Task implements ITaskLike, Runnable {

	/**
	 * Schedules this task to run ASAP.
	 */
	@Override
	public final void schedule() {
		TaskSystem.executorService.execute(this);
	}
	
	/**
	 * Schedules this task to run N milliseconds in the future.
	 * 
	 * @param milliseconds the number of milliseconds to run in the future
	 */
	@Override
	public final void scheduleRelative(long milliseconds) {
		TaskSystem.executorService.schedule(this, milliseconds, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules this task to run in N milliseconds.
	 * 
	 * @param delay the delay to wait before execution
	 * @param timeUnit the unit used for the delay
	 */
	@Override
	public final void scheduleRelative(long delay, TimeUnit timeUnit) {
		TaskSystem.executorService.schedule(this, delay, timeUnit);
	}
	
}
