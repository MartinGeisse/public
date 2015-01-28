/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.task;

import java.util.concurrent.TimeUnit;

/**
 * Keeps a simple array of tasks or task-likes and allows to schedule them
 * all at once.
 */
public class TaskArray implements ITaskLike {

	/**
	 * the tasks
	 */
	private ITaskLike[] tasks;

	/**
	 * Constructor.
	 */
	public TaskArray() {
	}
	
	/**
	 * Constructor.
	 * @param tasks the tasks
	 */
	public TaskArray(final ITaskLike[] tasks) {
		this.tasks = tasks;
	}

	/**
	 * Getter method for the tasks.
	 * @return the tasks
	 */
	public ITaskLike[] getTasks() {
		return tasks;
	}
	
	/**
	 * Setter method for the tasks.
	 * @param tasks the tasks to set
	 */
	public void setTasks(ITaskLike[] tasks) {
		this.tasks = tasks;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.ITaskLike#schedule()
	 */
	@Override
	public void schedule() {
		for (final ITaskLike task : tasks) {
			task.schedule();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.ITaskLike#scheduleRelative(long)
	 */
	@Override
	public void scheduleRelative(final long milliseconds) {
		for (final ITaskLike task : tasks) {
			task.scheduleRelative(milliseconds);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.ITaskLike#scheduleRelative(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public void scheduleRelative(final long delay, final TimeUnit timeUnit) {
		for (final ITaskLike task : tasks) {
			task.scheduleRelative(delay, timeUnit);
		}
	}

}
