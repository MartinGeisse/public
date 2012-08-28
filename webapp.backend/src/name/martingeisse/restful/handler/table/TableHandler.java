/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.handler.table;

import name.martingeisse.restful.handler.IRequestHandler;
import name.martingeisse.restful.handler.RequestParametersException;
import name.martingeisse.restful.request.RequestCycle;
import name.martingeisse.restful.request.RequestPathChain;

/**
 * This handler provides tabular data from an {@link ITableDataProvider}
 * and renders it in a tabular format as specified by an {@link ITableRenderer}.
 * 
 * An example for a table data provider would be an SQL table.
 * 
 * Examples for table renderers include JSON array-of-objects and CSV files.
 * 
 * This class selects a table renderer from request parameters. Subclasses
 * may override this behavior and/or provide additional renderers. The data
 * provider must be specified by subclasses or clients since there is no
 * default implementation.
 */
public class TableHandler implements IRequestHandler {

	/**
	 * the dataProvider
	 */
	private ITableDataProvider dataProvider;

	/**
	 * Constructor.
	 */
	public TableHandler() {
	}

	/**
	 * Constructor.
	 * @param dataProvider the dataProvider to set
	 */
	public TableHandler(final ITableDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * Getter method for the dataProvider.
	 * @return the dataProvider
	 */
	public ITableDataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * Setter method for the dataProvider.
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(final ITableDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * Determines the table renderer from the specified renderer selector.
	 * @param selector the selector, or null to choose the default renderer
	 * @return the renderer
	 */
	public ITableRenderer determineRenderer(String selector) {
		if (selector == null || selector.equals("json")) {
			return new JsonTableRenderer();
		} else if (selector.equals("csv")) {
			return new CsvTableRenderer();
		} else {
			throw new RequestParametersException("unknown table output format: " + selector);
		}
	}

	/**
	 * Determines the table renderer from the specified request cycle.
	 * This is done by taking the "format" parameter from the request's
	 * query string and invoking determineRenderer() on that selector.
	 * @param requestCycle the request cycle
	 * @return the renderer
	 */
	public ITableRenderer determineRenderer(RequestCycle requestCycle) {
		return determineRenderer(requestCycle.getRequest().getParameter("format"));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.IRequestHandler#handle(name.martingeisse.restful.request.RequestCycle, name.martingeisse.restful.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		ITableCursor cursor = dataProvider.fetch(requestCycle);
		ITableRenderer renderer = determineRenderer(requestCycle);
		renderer.render(cursor, requestCycle.getWriter());
	}

}
