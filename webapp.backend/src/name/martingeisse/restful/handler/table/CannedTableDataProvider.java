/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import name.martingeisse.restful.type.IFieldType;
import name.martingeisse.restful.util.IParameterSet;

/**
 * A table provider that stores a fixed data set and just returns it.
 */
public class CannedTableDataProvider implements ITableDataProvider {

	/**
	 * the columnTypes
	 */
	private IFieldType[] columnTypes;
	
	/**
	 * the data
	 */
	private Object[][] data;
	
	/**
	 * Constructor.
	 * @param columnTypes the column types
	 * @param data the canned data set
	 */
	public CannedTableDataProvider(IFieldType[] columnTypes, Object[][] data) {
		this.columnTypes = columnTypes;
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.table.ITableDataProvider#fetch(name.martingeisse.restful.util.IParameterSet)
	 */
	@Override
	public ITableCursor fetch(IParameterSet parameters) {
		return new CannedTableCursor(columnTypes, data);
	}

}
