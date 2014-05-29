/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.task;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * This class provides a blocking mechanism that waits until the barrier
 * task has been executed N times. Executing this task after that has
 * no effect. The task can be run "in-line", within another task, by
 * directly calling {@link #run()}.
 */
public final class TaskBarrier extends Task {

	/**
	 * the latch
	 */
	private final CountDownLatch latch;
	
	/**
	 * Constructor.
	 * @param times the number of times the barrier task has to be executed
	 * until the waiting threads are allowed to continue.
	 */
	public TaskBarrier(int times) {
		this.latch = new CountDownLatch(times);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		latch.countDown();
	}

	/**
	 * Waits until the barrier task has been executed often enough.
	 * @throws InterruptedException if interrupted while waiting
	 */
	public void await() throws InterruptedException {
		latch.await();
	}
	
	/**
	 * Waits until the barrier task has been executed often enough,
	 * or until the specified timeout occurs.
	 * 
	 * @param timeout the time to wait
	 * @param timeUnit the time unit
	 * @throws InterruptedException if interrupted while waiting
	 */
	public void await(long timeout, TimeUnit timeUnit) throws InterruptedException {
		latch.await(timeout, timeUnit);
	}
	
}
