/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.concurrency;

import java.util.LinkedList;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * This is a synchronization scheme that collects units of work
 * (of type T) and executes them all at once in a separate
 * thread. The current implementation uses a single executor
 * thread for all instances of this class.
 *
 * It works as follows: Threads that produce units of work put
 * them into this queue. The first unit starts a timer (configurable
 * in the constructor). Subsequent units can be added but do not
 * affect the timer. When the timer has expired, this class
 * atomically makes a snapshot of all queue items, empties the
 * queue, and resets the timer. It then calls a subclass method
 * with the collected items. Adding new items then starts a new
 * cycle. The exact sequence is such that a new cycle can be
 * started while the subclass callback is still executing.
 * 
 * @param <T> the work unit type
 */
public abstract class DelayBundledWorkQueue<T> implements Delayed {

	/**
	 * the activeQueues
	 */
	private static DelayQueue<DelayBundledWorkQueue<?>> activeQueues = new DelayQueue<DelayBundledWorkQueue<?>>();
	
	/**
	 * the delayMilliseconds
	 */
	private final long delayMilliseconds;
	
	/**
	 * the workUnits
	 */
	private final LinkedList<T> workUnits;
	
	/**
	 * the active
	 */
	private volatile boolean active;
	
	/**
	 * the executionTimeMilliseconds
	 */
	private volatile long executionTimeMilliseconds;
	
	/**
	 * Constructor.
	 * @param delayMilliseconds the number of milliseconds between
	 * the first item arriving in this queue and the subclass
	 * callback executing.
	 */
	public DelayBundledWorkQueue(long delayMilliseconds) {
		if (delayMilliseconds <= 0) {
			throw new IllegalArgumentException("delayMilliseconds must be > 0");
		}
		this.delayMilliseconds = delayMilliseconds;
		this.workUnits = new LinkedList<T>();
		this.active = false;
	}
	
	/**
	 * Adds a unit of work.
	 * @param workUnit the work unit to add
	 */
	public synchronized void add(T workUnit) {
		workUnits.add(workUnit);
		if (!active) {
			activate();
		}
	}

	/**
	 * 
	 */
	private void activate() {
		active = true;
		executionTimeMilliseconds = System.currentTimeMillis() + delayMilliseconds;
		activeQueues.add(this);
	}
	
	/**
	 * 
	 */
	private void expired() {
		
		// synchronize on this queue just long enough to fetch the work items
		Object[] workUnits;
		synchronized(this) {
			workUnits = this.workUnits.toArray(new Object[this.workUnits.size()]);
			this.workUnits.clear();
			active = false;
		}
		
		// now execute the work units (this queue is ready to receive new units in the meantime)
		perform(workUnits);
		
	}
	
	/**
	 * This method must be implemented by the subclass to actually perform
	 * the specified work. It is executed by the worker thread of this
	 * class.
	 * 
	 * Note that this class does not require this method to synchronize on
	 * this object. This is allowed however, as is adding new work units
	 * for the next cycle.
	 * 
	 * @param workUnits the collected work units (this isn't a T[] due to
	 * limitations in Java's type system).
	 */
	protected abstract void perform(Object[] workUnits);

	/* (non-Javadoc)
	 * @see java.util.concurrent.Delayed#getDelay(java.util.concurrent.TimeUnit)
	 */
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(executionTimeMilliseconds - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Delayed other) {
		long delay = getDelay(TimeUnit.MILLISECONDS);
		long otherDelay = other.getDelay(TimeUnit.MILLISECONDS);
		return Long.valueOf(delay).compareTo(Long.valueOf(otherDelay));
	}

	// start an executor thread that waits for timed-out work queues
	static {
		Thread executorThread = new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						activeQueues.take().expired();
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		executorThread.start();
	}

}
