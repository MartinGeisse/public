/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import java.io.PrintWriter;

import name.martingeisse.restful.type.IFieldType;

/**
 * Renders table data as a big JSON array-of-objects.
 * Field values may be rendered as JSON structures,
 * not necessarily primitive values.
 */
public class JsonTableRenderer implements ITableRenderer {

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.table.ITableRenderer#render(name.martingeisse.restful.handler.table.ITableCursor, java.io.PrintWriter)
	 */
	@Override
	public void render(ITableCursor tableCursor, PrintWriter out) {
		out.println("[");
		IFieldType[] types = tableCursor.getColumnTypes();
		boolean firstRow = true;
		while (true) {
			
			// fetch another row
			Object[] row = tableCursor.fetchRow();
			if (row == null) {
				break;
			}
			if (types.length != row.length) {
				System.out.println("row length mismatch; row length = " + row.length + ", columns = " + types.length);
				return;
			}
			
			// print row separation
			if (firstRow) {
				firstRow = false;
				out.print("\t{");
			} else {
				out.print("},\n\t{");
			}
			
			// print this row
			boolean firstCell = true;
			for (int i=0; i<types.length; i++) {
				if (firstCell) {
					firstCell = false;
				} else {
					out.print(", ");
				}
				out.print(types[i].convertToJson(row[i]));
			}
		}
		out.print("}\n]\n");
	}

}
