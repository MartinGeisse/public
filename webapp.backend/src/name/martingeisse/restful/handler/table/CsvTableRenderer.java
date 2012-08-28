/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import java.io.PrintWriter;

import name.martingeisse.restful.type.IFieldType;

/**
 * This {@link ITableRenderer} produces a CSV file from the table.
 */
public class CsvTableRenderer implements ITableRenderer {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.table.ITableRenderer#render(name.martingeisse.restful.handler.table.ITableCursor, java.io.PrintWriter)
	 */
	@Override
	public void render(ITableCursor tableCursor, PrintWriter out) {
		IFieldType[] types = tableCursor.getColumnTypes();
		while (true) {
			Object[] row = tableCursor.fetchRow();
			if (row == null) {
				break;
			}
			if (types.length != row.length) {
				System.out.println("row length mismatch; row length = " + row.length + ", columns = " + types.length);
				return;
			}
			boolean first = true;
			for (int i=0; i<types.length; i++) {
				if (first) {
					first = false;
				} else {
					out.print('|');
				}
				String value = types[i].convertToText(row[i]);
				out.print(value.replace("\\", "\\\\").replace("|", "\\|"));
			}
			out.println();
		}
	}

}
