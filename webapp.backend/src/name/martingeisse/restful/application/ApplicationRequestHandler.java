/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.application;

import name.martingeisse.restful.handler.IRequestHandler;
import name.martingeisse.restful.handler.NamedResourceFolderHandler;
import name.martingeisse.restful.handler.RequestDumpHandler;
import name.martingeisse.restful.handler.RequestParametersException;
import name.martingeisse.restful.handler.table.CannedTableDataProvider;
import name.martingeisse.restful.handler.table.TableHandler;
import name.martingeisse.restful.jdbc.SimpleJdbcTableDataProvider;
import name.martingeisse.restful.jdbc.query.SingleColumnAsArrayQuery;
import name.martingeisse.restful.servlet.RestfulServlet;
import name.martingeisse.restful.type.ArrayFieldType;
import name.martingeisse.restful.type.BooleanFieldType;
import name.martingeisse.restful.type.IFieldType;
import name.martingeisse.restful.type.IntegerFieldType;
import name.martingeisse.restful.type.StringFieldType;
import name.martingeisse.restful.util.IParameterSet;

/**
 * The main application request handler. The {@link RestfulServlet}
 * expects this class to have a no-argument constructor and to implement
 * {@link IRequestHandler}.
 */
public class ApplicationRequestHandler extends NamedResourceFolderHandler {

	/**
	 * Constructor.
	 */
	public ApplicationRequestHandler() {
		NamedResourceFolderHandler subHandler = new NamedResourceFolderHandler();
		subHandler.getResources().put("foo", new RequestDumpHandler());
		subHandler.getResources().put("bar", new RequestDumpHandler());
		getResources().put("sub", subHandler);
		getResources().put("baz", new RequestDumpHandler());
		
		IFieldType[] columnTypes = new IFieldType[] {
			new IntegerFieldType(),
			new StringFieldType(),
			new BooleanFieldType()
		};
		Object[][] data = new Object[][] {
			{1, "Foo", false},
			{23, "Bar", true},
			{42, "Baz", true},
			{255, new Integer(150), true},
		};
		CannedTableDataProvider dataProvider = new CannedTableDataProvider(columnTypes, data);
		TableHandler table = new TableHandler(dataProvider);
		getResources().put("table", table);

		getResources().put("table2", new TableHandler(new MyTableDataProvider()));
		
	}
	
	private class MyTableDataProvider extends SimpleJdbcTableDataProvider {
		
		/**
		 * Constructor.
		 */
		public MyTableDataProvider() {
			super(new IFieldType[] {
				new IntegerFieldType(),
				new StringFieldType(),
				new ArrayFieldType(new StringFieldType()),
			}, "SELECT \"id\", \"name\" FROM \"FoodProvider\" $o");
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.restful.jdbc.SimpleJdbcTableDataProvider#transformQuery(java.lang.String, name.martingeisse.restful.util.IParameterSet)
		 */
		@Override
		protected String transformQuery(String originalQuery, IParameterSet parameters) {
			final String order = parameters.getParameter("order");
			final String orderClause;
			if (order == null) {
				orderClause = "";
			} else if (order.equals("id")) {
				orderClause = "ORDER BY \"id\"";
			} else if (order.equals("name")) {
				orderClause = "ORDER BY \"name\"";
			} else {
				throw new RequestParametersException("Invalid order: " + order);
			}
			return originalQuery.replace("$o", orderClause);
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.restful.jdbc.SimpleJdbcTableDataProvider#postProcessRow(java.lang.Object[], name.martingeisse.restful.util.IParameterSet)
		 */
		@Override
		protected void postProcessRow(Object[] row, IParameterSet parameters) {
			if (parameters.getParameter("sub") != null) {
				row[2] = SingleColumnAsArrayQuery.execute("SELECT \"name\" FROM \"MenuItem\" WHERE \"foodProvider_id\" = " + row[0]);
			} else {
				row[2] = new Object[0];
			}
		}
		
	}
	
}
