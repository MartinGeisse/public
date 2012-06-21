/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.keycount;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.document.IBlockItem;
import name.martingeisse.reporting.document.Table;

/**
 * TODO: test only
 */
public class KeyCountTestTable implements IBlockItem {

	/**
	 * the query
	 */
	private IKeyCountQuery query;

	/**
	 * the caption
	 */
	private String caption;

	/**
	 * Constructor.
	 */
	public KeyCountTestTable() {
	}

	/**
	 * Constructor.
	 * @param query the query that is used to fill this table with data
	 */
	public KeyCountTestTable(final IKeyCountQuery query) {
		this.query = query;
	}

	/**
	 * Constructor.
	 * @param query the query that is used to fill this table with data
	 * @param caption the table caption
	 */
	public KeyCountTestTable(final IKeyCountQuery query, final String caption) {
		this.query = query;
		this.caption = caption;
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public IKeyCountQuery getQuery() {
		return query;
	}

	/**
	 * Setter method for the query.
	 * @param query the query to set
	 */
	public void setQuery(final IKeyCountQuery query) {
		this.query = query;
	}

	/**
	 * Getter method for the caption.
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Setter method for the caption.
	 * @param caption the caption to set
	 */
	public void setCaption(final String caption) {
		this.caption = caption;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public Table bindToData(final DataSources dataSources) {
		final Table result = new Table();
		result.setCaption(caption);
		result.setFieldNames(new String[] {"Key", "Count"});
		final IKeyCountResultSet resultSet = query.bindToData(dataSources);
		while (resultSet.next()) {
			KeyCountEntry entry = resultSet.get();
			result.getRows().add(new String[] {entry.getKey(), "" + entry.getCount()});
		}
		resultSet.close();
		return result;
	}

}
