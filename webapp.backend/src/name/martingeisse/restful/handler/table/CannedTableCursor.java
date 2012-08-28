/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import name.martingeisse.restful.type.IFieldType;

/**
 * An {@link ITableCursor} that provides data specified
 * in the constructor.
 */
public class CannedTableCursor implements ITableCursor {

	/**
	 * the columnTypes
	 */
	private IFieldType[] columnTypes;
	
	/**
	 * the data
	 */
	private Object[][] data;

	/**
	 * the currentRowIndex
	 */
	private int currentRowIndex;
	
	/**
	 * the stopRowIndex
	 */
	private int stopRowIndex;

	/**
	 * Constructor.
	 * @param columnTypes the column types
	 * @param data the canned table data
	 */
	public CannedTableCursor(IFieldType[] columnTypes, Object[][] data) {
		this(columnTypes, data, 0, data.length);
	}

	/**
	 * Constructor.
	 * @param columnTypes the column types
	 * @param data the canned table data
	 * @param startRowIndex the index of the first row that is returned by this cursor
	 * @param stopRowIndex the index of the first row after visible data that is not returned
	 */
	public CannedTableCursor(IFieldType[] columnTypes, Object[][] data, int startRowIndex, int stopRowIndex) {
		
		// check arguments
		if (startRowIndex < 0) {
			throw new IllegalArgumentException("invalid start row index: " + startRowIndex);
		}
		if (stopRowIndex < startRowIndex) {
			throw new IllegalArgumentException("invalid stop row index: " + stopRowIndex + ", start is: " + startRowIndex);
		}
		
		// initialize
		this.columnTypes = columnTypes;
		this.data = data;
		this.currentRowIndex = startRowIndex;
		this.stopRowIndex = stopRowIndex;
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.table.ITableCursor#getColumnTypes()
	 */
	@Override
	public IFieldType[] getColumnTypes() {
		return columnTypes;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.table.ITableCursor#fetchRow()
	 */
	@Override
	public Object[] fetchRow() {
		if (currentRowIndex == stopRowIndex) {
			return null;
		}
		Object[] result = data[currentRowIndex];
		currentRowIndex++;
		return result;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.table.ITableCursor#close()
	 */
	@Override
	public void close() {
	}
	
}
