/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.jdbc.query;

import java.util.Iterator;

/**
 * This interface is used by queries that fetch results one after
 * another, cursor-like. 
 * 
 * Using an instance as an iterator loops through the results.
 * It does not invoke either start() or stop() -- clients must
 * do so manually. The remove() method of {@link Iterator} is
 * not supported.
 * 
 * Using an instance as an iterable has the effect of returning
 * itself as an iterator. Clients must be careful not to request
 * "multiple iterators" since this instance is returned each time.
 * 
 * @param <T> the result type
 */
public interface ISequenceQuery<T> extends Iterable<T>, Iterator<T> {

	/**
	 * Executes this query. This method must be called before
	 * fetching any rows. Calling this method again while this
	 * query is running has the same effect as calling stop()
	 * before restarting.
	 */
	public void start();
	
	/**
	 * Stops this query. No more rows can be fetched after calling
	 * this method. Clients should use this method when they are
	 * done with the query to dispose of SQL resources.
	 */
	public void stop();
	
}
