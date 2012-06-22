/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.document.AbstractTable;
import name.martingeisse.reporting.document.Table;

/**
 * This item is similar to a {@link Table} but does not yet contain its
 * data. Instead, it is fileld with data from an {@link ITabularQuery}.
 */
public class UnboundTable extends AbstractTable {

	/**
	 * the query
	 */
	private ITabularQuery query;

	/**
	 * Constructor.
	 */
	public UnboundTable() {
	}

	/**
	 * Constructor.
	 * @param query the query that is used to fill this table with data
	 */
	public UnboundTable(final ITabularQuery query) {
		this.query = query;
	}

	/**
	 * Constructor.
	 * @param query the query that is used to fill this table with data
	 * @param caption the table caption
	 */
	public UnboundTable(final ITabularQuery query, final String caption) {
		this.query = query;
		setCaption(caption);
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public ITabularQuery getQuery() {
		return query;
	}

	/**
	 * Setter method for the query.
	 * @param query the query to set
	 */
	public void setQuery(final ITabularQuery query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public Table bindToData(final DataSources dataSources) {
		try {

			// perform the query
			final ResultSet resultSet = query.bindToData(dataSources);
			final ResultSetMetaData meta = resultSet.getMetaData();
			final int width = meta.getColumnCount();

			// generate headers
			final String[] fieldNames = new String[width];
			for (int i = 0; i < width; i++) {
				fieldNames[i] = meta.getColumnLabel(1 + i);
			}

			// fill the result table
			final Table result = new Table();
			result.setCaption(getCaption());
			result.setFieldNames(fieldNames);
			while (resultSet.next()) {
				final String[] values = new String[width];
				for (int i = 0; i < width; i++) {
					values[i] = resultSet.getString(1 + i);
				}
				result.getRows().add(values);
			}
			resultSet.close();
			return result;

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
