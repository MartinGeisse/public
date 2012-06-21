/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.keycount;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.definition.ITabularQuery;

/**
 * This class adapts an {@link ITabularQuery} to {@link IKeyCountQuery}
 * behavior. It expects the key in the first column and the count in the second.
 * This class does not actually count anything; this is expected to happen
 * as part of the wrapped query.
 */
public class SimpleTabularKeyCountQueryAdapter implements IKeyCountQuery {

	/**
	 * the wrappedQuery
	 */
	private ITabularQuery wrappedQuery;

	/**
	 * Constructor.
	 */
	public SimpleTabularKeyCountQueryAdapter() {
	}

	/**
	 * Constructor.
	 * @param wrappedQuery the wrapped query
	 */
	public SimpleTabularKeyCountQueryAdapter(final ITabularQuery wrappedQuery) {
		this.wrappedQuery = wrappedQuery;
	}

	/**
	 * Getter method for the wrappedQuery.
	 * @return the wrappedQuery
	 */
	public ITabularQuery getWrappedQuery() {
		return wrappedQuery;
	}

	/**
	 * Setter method for the wrappedQuery.
	 * @param wrappedQuery the wrappedQuery to set
	 */
	public void setWrappedQuery(final ITabularQuery wrappedQuery) {
		this.wrappedQuery = wrappedQuery;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.keycount.IKeyCountQuery#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public IKeyCountResultSet bindToData(final DataSources dataSources) {
		return new SimpleTabularKeyCountResultSetAdapter(wrappedQuery.bindToData(dataSources));
	}

}
