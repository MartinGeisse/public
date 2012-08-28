/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.jdbc.query;

/**
 * A query that executes at once and returns a single Java object as
 * its result. Note that queries which collect rows in an array
 * or list are also reduction queries since they also return
 * a single object at once (as opposed to fetching rows cursor-like),
 * as well as queries that use SQL-side reduction (such as count queries).
 * @param <T> the type of the reduced value
 */
public interface IReductionQuery<T> {

	/**
	 * Executes this query.
	 * @return the reduced result.
	 */
	public T execute();
	
}
