/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A collection of tasks to operate on a collection or array of objects.
 * This class creates a task for each element of the collection at creation
 * time and allows to schedule those tasks for execution. If the collection
 * is large, then this large number of objects might put too much stress
 * on the GC and {@link LargeCollectionTask} might be more suitable.
 * 
 * The collection supports adding a "followup" task that will be invoked
 * when all tasks for the collection have completed. However, collection
 * tasks with a followup task must not be started more than once in parallel
 * because finished tasks from different runs will become mixed up, and
 * the followup task will be run too early. Similarly, the followup task
 * must not be switched while the collection task is running or scheduled.
 * 
 * Note that the {@link #handleElement(Object)} method may be invoked
 * concurrently for multiple elements -- this is the whole point of using
 * this class.
 * 
 * @param <T> the type of collection elements
 */
public abstract class SmallCollectionTask<T> extends TaskArray {
	
	/**
	 * the pendingTasks
	 */
	private final AtomicInteger pendingTasks = new AtomicInteger();

	/**
	 * the followupTask
	 */
	private ITaskLike followupTask;
	
	/**
	 * Constructor.
	 * @param elements the elements to handle
	 */
	public SmallCollectionTask(final T[] elements) {
		Task[] tasks = new Task[elements.length];
		for (int i = 0; i < elements.length; i++) {
			tasks[i] = new ElementTask(elements[i]);
		}
		super.setTasks(tasks);
	}

	/**
	 * Constructor.
	 * @param elements the elements to handle
	 */
	public SmallCollectionTask(final Iterable<T> elements) {
		this(elements.iterator());
	}

	/**
	 * Constructor.
	 * @param elements the elements to handle
	 */
	public SmallCollectionTask(final Iterator<T> elements) {
		final ArrayList<Task> taskList = new ArrayList<Task>();
		while (elements.hasNext()) {
			taskList.add(new ElementTask(elements.next()));
		}
		super.setTasks(taskList.toArray(new Task[taskList.size()]));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.TaskArray#setTasks(name.martingeisse.stackd.common.task.ITaskLike[])
	 */
	@Override
	public void setTasks(ITaskLike[] tasks) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Getter method for the followupTask.
	 * @return the followupTask
	 */
	public ITaskLike getFollowupTask() {
		return followupTask;
	}
	
	/**
	 * Setter method for the followupTask.
	 * 
	 * Setting the followup task will fail if the collection task is currently
	 * running.
	 * 
	 * @param followupTask the followupTask to set
	 * @return this for chaining
	 */
	public synchronized SmallCollectionTask<T> setFollowupTask(ITaskLike followupTask) {
		if (pendingTasks.get() != 0) {
			throw new IllegalStateException("this collection task has pending element tasks");
		}
		this.followupTask = followupTask;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.TaskArray#schedule()
	 */
	@Override
	public void schedule() {
		prepare();
		super.schedule();
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.TaskArray#scheduleRelative(long)
	 */
	@Override
	public void scheduleRelative(long milliseconds) {
		prepare();
		super.scheduleRelative(milliseconds);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.TaskArray#scheduleRelative(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public void scheduleRelative(long delay, TimeUnit timeUnit) {
		prepare();
		super.scheduleRelative(delay, timeUnit);
	}
	
	/**
	 * This method throws an exception if there is a followup task and pending element tasks. Thus,
	 * after calling this method, there are three cases:
	 * 
	 * - There is no followup task and no pending element tasks. We can safely schedule this
	 *   collection task. This creates element tasks which we mark as pending, but nothing
	 *   happens when the counter reaches zero. They are counted only to handle the second
	 *   case below:
	 *   
	 * - There is no followup tasks, but there are pending element tasks. We can still safely
	 *   schedule this collection task, but we have to *add* the number of pending element
	 *   tasks from the two runs, so we know exactly when the number of pending tasks
	 *   actually reaches zero, for the safety checks.
	 *   
	 * - There is a followup task but no pending element tasks. We can safely schedule this
	 *   collection task, creating element tasks, and when they are all finished the followup
	 *   task gets scheduled. Replacing the followup task in the meantime would throw an
	 *   exception, as would re-scheduling the collection task. Thus we can be sure that
	 *   the followup task will stay the same until it gets executed, without copying it.
	 */
	private synchronized void prepare() {
		if (followupTask != null && pendingTasks.get() != 0) {
			throw new IllegalStateException("collection task has pending element tasks and a followup task -- cannot reschedule until finished");
		}
		pendingTasks.addAndGet(getTasks().length);
	}
	
	/**
	 * This method must be implemented by subclasses to provide the actual
	 * task logic.
	 * @param element the element to handle
	 */
	protected abstract void handleElement(T element);

	/**
	 * The task implementation.
	 */
	private final class ElementTask extends Task {

		/**
		 * the element
		 */
		private final T element;

		/**
		 * Constructor.
		 * @param element the element
		 */
		public ElementTask(final T element) {
			this.element = element;
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			handleElement(element);
			
			// at this point we have to keep the followup task in a local variable,
			// because decrementing the "pending element task" counter might set
			// that counter to 0 and thus allow replacing the followup task in the
			// enclosing class. Also, at this point we have to exploit the
			// atomic "set and get" functionality of AtomicInteger to prevent
			// multiple "last" tasks from scheduling the followup task multiple times.
			ITaskLike followupTask = SmallCollectionTask.this.followupTask;
			int remaining = pendingTasks.decrementAndGet();
			if (remaining == 0) {
				followupTask.schedule();
			}
			
		}

	}

}
