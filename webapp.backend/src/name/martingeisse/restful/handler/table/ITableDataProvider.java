/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import name.martingeisse.restful.util.IParameterSet;

/**
 * A data provider that provides a number of data records.
 * Each record has the same structure as any other record.
 * The records are organized as rows and columns of a table, 
 * with different rows representing the records and different
 * columns representing the fields of the records.
 *
 * To fetch data, an {@link ITableCursor} must be obtained
 * from this data provider, then rows can be fetched from
 * the cursor. The cursor keeps the state of a single
 * query, while this interface represents the (persistent)
 * table and is considered stateless except for the table
 * contents.
 * 
 * The actual shape of the table, as returned by the cursor,
 * optionally depends on parameters that are specified when
 * the cursor is created.
 * 
 * Note that this interface is designed to allow general access
 * to all kinds of tabular data. If client code has specific
 * knowledge about the data source, it might be easier to
 * directly create a specific {@link ITableCursor} instance.
 */
public interface ITableDataProvider {

	/**
	 * Fetches data from this provider. The parameters control the
	 * actual shape of the table that is provided.
	 * @param parameters the parameters
	 * @return the cursor
	 */
	public ITableCursor fetch(IParameterSet parameters);
	
}
