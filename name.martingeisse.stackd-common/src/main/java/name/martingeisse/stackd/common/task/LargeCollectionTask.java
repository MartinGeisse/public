/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.task;

import java.util.Iterator;
import org.apache.commons.lang3.NotImplementedException;

/**
 * A collection of tasks to operate on a collection or array of objects.
 * 
 * The collection has a "parallelism" setting that controls the number of
 * concurrent tasks that will be scheduled in the task system. This parameter
 * should never be less than the number of threads in the task system,
 * otherwise there might be idle tasks that cannot pick up the pending
 * work because there are no task objects for it. The downside of a too
 * large value is that many task objects will be created and disposed,
 * causing stress on the GC. The default value is 20.
 * 
 * The collection supports adding a "followup" task that will be invoked
 * when all tasks for the collection have completed.
 * 
 * @param <T> the type of collection elements
 */
public abstract class LargeCollectionTask<T> {

	// TODO not implemented yet
	
	/**
	 * the elementIterator
	 */
	private Iterator<T> elementIterator;
	
	/**
	 * the currentTasks
	 */
	private Task[] currentTasks;
	
	/**
	 * Constructor.
	 * @param elements the elements to handle
	 */
	public LargeCollectionTask(T[] elements) {
		throw new NotImplementedException("");
	}
	
	/**
	 * Constructor.
	 * @param elements the elements to handle
	 * @param iteratorAlreadySynchronized whether the iterator returned from the 'elements' argument
	 * is thread-safe by itself (true) or requires synchronized access from the outside (false).
	 */
	public LargeCollectionTask(Iterable<T> elements, boolean iteratorAlreadySynchronized) {
		throw new NotImplementedException("");
	}
	
	/**
	 * Constructor.
	 * @param elements the elements to handle
	 * @param iteratorAlreadySynchronized whether the iterator returned from the 'elements' argument
	 * is thread-safe by itself (true) or requires synchronized access from the outside (false).
	 */
	public LargeCollectionTask(Iterator<T> elements, boolean iteratorAlreadySynchronized) {
		throw new NotImplementedException("");
	}
	
}
