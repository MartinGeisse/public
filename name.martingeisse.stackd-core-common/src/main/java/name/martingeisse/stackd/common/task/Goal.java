/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.task;

import java.util.concurrent.TimeUnit;
import name.martingeisse.common.util.SemaphoreWithPublicReducePermits;

/**
 * A goal is similar to a task, but can be scheduled only once. In turn,
 * goals support growing requirements while already running.
 * 
 * Goals use a distinct boolean flag for this that specifies whether the
 * goal and its requirements have been "sealed". A goal can be scheduled
 * and started at any point after creation, and can be extended until
 * it gets sealed. After that, no extensions are possible anymore. It is,
 * possible to schedule and start a goal *after* it has been sealed,
 * although in that case a simple task would be sufficient.
 * 
 * A goal can be waited for, and waiting only finishes when the goal has
 * been sealed *and* achieved.
 * 
 * Subclasses likely act differently at various points, depending on
 * whether this goal has been started yet. Changing the started flag
 * occurs in a synchronized(this) block, so such methods can also
 * synchronize on this object to prevent the goal from starting while
 * they are running.
 */
public abstract class Goal implements ITaskLike {
	
	/**
	 * the pendingLifecycleEvents
	 */
	private final SemaphoreWithPublicReducePermits pendingLifecycleEvents = new SemaphoreWithPublicReducePermits(-1);

	/**
	 * the scheduled
	 */
	private boolean scheduled = false;
	
	/**
	 * the started
	 */
	private boolean started = false;
	
	/**
	 * the sealed
	 */
	private boolean sealed = false;
	
	/**
	 * Schedules this goal to run ASAP.
	 */
	@Override
	public final void schedule() {
		prepareSchedule();
		TaskSystem.executorService.execute(new StartGoalRunnable());
	}
	
	/**
	 * Schedules this goal to run N milliseconds in the future.
	 * 
	 * @param milliseconds the number of milliseconds to run in the future
	 */
	@Override
	public final void scheduleRelative(long milliseconds) {
		prepareSchedule();
		TaskSystem.executorService.schedule(new StartGoalRunnable(), milliseconds, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Schedules this goal to run in N milliseconds.
	 * 
	 * @param delay the delay to wait before execution
	 * @param timeUnit the unit used for the delay
	 */
	@Override
	public final void scheduleRelative(long delay, TimeUnit timeUnit) {
		prepareSchedule();
		TaskSystem.executorService.schedule(new StartGoalRunnable(), delay, timeUnit);
	}
	
	/**
	 * 
	 */
	private synchronized void prepareSchedule() {
		if (scheduled) {
			throw new IllegalStateException("This goal has already been scheduled.");
		}
		scheduled = true;
	}
	
	/**
	 * Getter method for the started.
	 * @return the started
	 */
	protected synchronized final boolean isStarted() {
		return started;
	}
	
	/**
	 * Starts working on this goal. This method gets called when the point in time
	 * is reached for which this goal was scheduled.
	 */
	protected abstract void onStart();
	
	/**
	 * Throws an {@link IllegalStateException} if this goal has already been sealed.
	 */
	protected final synchronized void checkNotSealed() {
		if (sealed) {
			throw new IllegalStateException("This goal has already been sealed.");
		}
	}

	/**
	 * Seals this goal.
	 */
	public final synchronized void seal() {
		checkNotSealed();
		sealed = true;
		pendingLifecycleEvents.release();
	}
	
	/**
	 * Notes down that another subgoal is needed to achieve this goal.
	 */
	protected final void noteSubgoalRequired() {
		pendingLifecycleEvents.reducePermits(1);
	}
	
	/**
	 * Notes down that another subgoal has been finished.
	 */
	protected final void noteSubgoalFinished() {
		pendingLifecycleEvents.release();
	}
	
	/**
	 * Waits until this goal has been achieved.
	 * @throws InterruptedException if interrupted while waiting
	 */
	public final void await() throws InterruptedException {
		pendingLifecycleEvents.acquire();
	}
	
	/**
	 * Waits until this goal has been achieved.
	 * @param timeout the timeout for waiting
	 * @param timeUnit the time unit
	 * @throws InterruptedException if interrupted while waiting
	 */
	public final void await(long timeout, TimeUnit timeUnit) throws InterruptedException {
		pendingLifecycleEvents.tryAcquire(timeout, timeUnit);
	}

	/**
	 * 
	 */
	class StartGoalRunnable implements Runnable {
		@Override
		public void run() {
			synchronized(Goal.this) {
				started = true;
			}
			onStart();
			pendingLifecycleEvents.release();
		}
	}
	
}
