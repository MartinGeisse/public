/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.task;

import java.util.ArrayList;

/**
 * Manages a set of subgoals that must be executed in parallel
 * to achieve the goal. Subgoals can be added to the set
 * until sealed.
 * 
 * TODO allow autostart without scheduling manually (thread
 * switch round-trip!)
 */
public final class ParallelGoal extends Goal {

	/**
	 * the subgoalTasks
	 */
	private ArrayList<Task> subgoalTasks = new ArrayList<Task>();

	/**
	 * Adds a subgoal to this goal.
	 * @param subgoal the subgoal to add
	 */
	public synchronized void addSubgoal(final Runnable subgoal) {
		checkNotSealed();
		noteSubgoalRequired();
		final Task subgoalTask = new Task() {
			@Override
			public void run() {
				subgoal.run();
				noteSubgoalFinished();
			}
		};
		if (isStarted()) {
			subgoalTask.schedule();
		} else {
			subgoalTasks.add(subgoalTask);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.common.task.Goal#onStart()
	 */
	@Override
	protected synchronized void onStart() {
		for (final Task subgoalTask : subgoalTasks) {
			subgoalTask.schedule();
		}
		subgoalTasks = null;
	}

}
