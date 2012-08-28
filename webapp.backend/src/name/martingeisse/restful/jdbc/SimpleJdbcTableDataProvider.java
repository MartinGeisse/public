/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import name.martingeisse.restful.handler.table.ITableCursor;
import name.martingeisse.restful.handler.table.ITableDataProvider;
import name.martingeisse.restful.type.IFieldType;
import name.martingeisse.restful.util.IParameterSet;

/**
 * An {@link ITableDataProvider} that uses a type array and an SQL query.
 * This class is intended to be simple rather than flexible.
 */
public class SimpleJdbcTableDataProvider implements ITableDataProvider {

	/**
	 * the types
	 */
	private final IFieldType[] types;
	
	/**
	 * the query
	 */
	private final String query;
	
	/**
	 * Constructor.
	 * 
	 * Note that the 'types' array may be larger than the number of columns
	 * returned by the query. This can be used in combination with overriding
	 * postProcessRow() to return additional values besides the results of the
	 * main query. All additional values are converted by the {@link IFieldType}
	 * objects just like plain SQL results.
	 * 
	 * @param types the field types
	 * @param query the query to execute
	 */
	public SimpleJdbcTableDataProvider(final IFieldType[] types, final String query) {
		this.types = types;
		this.query = query;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.restful.handler.table.ITableDataProvider#fetch(name.martingeisse.restful.util.IParameterSet)
	 */
	@Override
	public ITableCursor fetch(IParameterSet parameters) {
		return new MyCursor(parameters);
	}
	
	/**
	 * This method is used to create a customized query based on the original query
	 * from the constructor and the fetch parameters. The default implementation
	 * just returns the original query.
	 * 
	 * @param originalQuery the original query from the constructor
	 * @param parameters the parameters
	 * @return the modified query
	 */
	protected String transformQuery(String originalQuery, IParameterSet parameters) {
		return originalQuery;
	}
	
	/**
	 * Post-processes a table row right after fetching it. This allows subclasses to insert
	 * additional values (if the types array is larger than the rows returned by the query),
	 * or to modify fetched values. The default implementation does nothing.
	 * 
	 * @param row the row to post-process. Changes should be made in-place.
	 */
	protected void postProcessRow(Object[] row, IParameterSet parameters) {
	}

	/**
	 * The table cursor implementation.
	 */
	private class MyCursor implements ITableCursor {

		/**
		 * the parameters
		 */
		private IParameterSet parameters;
		
		/**
		 * the connection
		 */
		private Connection connection;
		
		/**
		 * the statement
		 */
		private Statement statement;
		
		/**
		 * the resultSet
		 */
		private ResultSet resultSet;

		/**
		 * Constructor.
		 * @param parameters the parameters used to create this cursor
		 */
		public MyCursor(IParameterSet parameters) {
			try {
				String customizedQuery = transformQuery(query, parameters);
				this.parameters = parameters;
				this.connection = JdbcConnectionManager.getConnection();
				this.statement = connection.createStatement();
				this.resultSet = statement.executeQuery(customizedQuery);
			} catch (SQLException e) {
				tryClose();
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Tries to close the connection and sets the connection field to null.
		 * Returns normally even if the connection cannot be closed.
		 */
		private void tryClose() {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
			}
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.restful.handler.table.ITableCursor#getColumnTypes()
		 */
		@Override
		public IFieldType[] getColumnTypes() {
			return types;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.restful.handler.table.ITableCursor#fetchRow()
		 */
		@Override
		public Object[] fetchRow() {
			try {
				if (!resultSet.next()) {
					return null;
				}
				Object[] result = new Object[types.length];
				int sqlColumnCount = resultSet.getMetaData().getColumnCount();
				for (int i=0; i<sqlColumnCount; i++) {
					result[i] = resultSet.getObject(1 + i);
				}
				postProcessRow(result, parameters);
				return result;
			} catch (SQLException e) {
				tryClose();
				throw new RuntimeException(e);
			}
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.restful.handler.table.ITableCursor#close()
		 */
		@Override
		public void close() {
			tryClose();
		}
		
	}

}
