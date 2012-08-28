/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import java.io.PrintWriter;

/**
 * Implementations know how to render data from an {@link ITableCursor}
 * to response text.
 */
public interface ITableRenderer {

	/**
	 * Renders data from the cursor to the print writer.
	 * @param tableCursor the cursor to read data from
	 * @param out the writer to write to.
	 */
	public void render(ITableCursor tableCursor, PrintWriter out);
	
}
