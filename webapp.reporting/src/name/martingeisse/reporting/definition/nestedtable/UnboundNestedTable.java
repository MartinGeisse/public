/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.document.AbstractFigureItem;
import name.martingeisse.reporting.document.NestedTable;

/**
 * A renderable nested table.
 */
public class UnboundNestedTable extends AbstractFigureItem {

	/**
	 * the data
	 */
	private INestedTableQuery query;

	/**
	 * Constructor.
	 */
	public UnboundNestedTable() {
	}

	/**
	 * Constructor.
	 * @param query the nested-table query
	 */
	public UnboundNestedTable(final INestedTableQuery query) {
		this.query = query;
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public INestedTableQuery getQuery() {
		return query;
	}

	/**
	 * Setter method for the query.
	 * @param query the query to set
	 */
	public void setQuery(final INestedTableQuery query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public NestedTable bindToData(final DataSources dataSources) {
		return new NestedTable(query.bindToData(dataSources).makeExplicitOnDemand());
	}

}
