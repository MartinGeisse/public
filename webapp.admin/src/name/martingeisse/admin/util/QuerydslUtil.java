/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.Wildcard;

/**
 * Utility methods to deal with QueryDSL's objects.
 */
public class QuerydslUtil {

	/**
	 * Prevent instantiation.
	 */
	private QuerydslUtil() {
	}
	
	/**
	 * Dumps the query text and parameters from the specified query.
	 * @param query the query to dump
	 */
	public static void dumpQuery(SQLQueryImpl query) {
		
		Connection connection = new MyConnection();
		SQLQuery clone = new SQLQueryImpl(connection, new MySQLTemplates(), query.getMetadata()) {
			
			/* (non-Javadoc)
			 * @see com.mysema.query.sql.AbstractSQLQuery#setParameters(java.sql.PreparedStatement, java.util.List, java.util.List, java.util.Map)
			 */
			@Override
			protected void setParameters(PreparedStatement stmt, List<?> objects, List<Path<?>> constantPaths, Map<ParamExpression<?>, ?> params) {
				
				System.out.println("objects: ");
				for (Object o : objects) {
					System.out.println("* " + o);
				}
				
				System.out.println("constant paths: ");
				for (Path<?> path : constantPaths) {
					System.out.println("* " + path);
				}
				
				System.out.println("params: ");
				for (Map.Entry<ParamExpression<?>, ?> param : params.entrySet()) {
					System.out.println("* " + param.getKey() + " -> " + param.getValue());
				}
				
				throw new MyAbortException();
			}
			
		};
		
		try {
			clone.getResults(Wildcard.all);
		} catch (MyAbortException e) {
		}
		
	}

	/**
	 * This exception type is used to return directly after dumping parameters.
	 */
	private static class MyAbortException extends RuntimeException {
	}
	
	/**
	 * Fake {@link Connection} implementation.
	 */
	private static class MyConnection implements Connection {

		/* (non-Javadoc)
		 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
		 */
		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return false;
		}

		/* (non-Javadoc)
		 * @see java.sql.Wrapper#unwrap(java.lang.Class)
		 */
		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#clearWarnings()
		 */
		@Override
		public void clearWarnings() throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#close()
		 */
		@Override
		public void close() throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#commit()
		 */
		@Override
		public void commit() throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
		 */
		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createBlob()
		 */
		@Override
		public Blob createBlob() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createClob()
		 */
		@Override
		public Clob createClob() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createNClob()
		 */
		@Override
		public NClob createNClob() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createSQLXML()
		 */
		@Override
		public SQLXML createSQLXML() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStatement()
		 */
		@Override
		public Statement createStatement() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStatement(int, int, int)
		 */
		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStatement(int, int)
		 */
		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
		 */
		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getAutoCommit()
		 */
		@Override
		public boolean getAutoCommit() throws SQLException {
			return false;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getCatalog()
		 */
		@Override
		public String getCatalog() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getClientInfo()
		 */
		@Override
		public Properties getClientInfo() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getClientInfo(java.lang.String)
		 */
		@Override
		public String getClientInfo(String name) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getHoldability()
		 */
		@Override
		public int getHoldability() throws SQLException {
			return 0;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getMetaData()
		 */
		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getTransactionIsolation()
		 */
		@Override
		public int getTransactionIsolation() throws SQLException {
			return 0;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getTypeMap()
		 */
		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#getWarnings()
		 */
		@Override
		public SQLWarning getWarnings() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#isClosed()
		 */
		@Override
		public boolean isClosed() throws SQLException {
			return false;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#isReadOnly()
		 */
		@Override
		public boolean isReadOnly() throws SQLException {
			return false;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#isValid(int)
		 */
		@Override
		public boolean isValid(int timeout) throws SQLException {
			return false;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#nativeSQL(java.lang.String)
		 */
		@Override
		public String nativeSQL(String sql) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
		 */
		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
		 */
		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareCall(java.lang.String)
		 */
		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
		 */
		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#prepareStatement(java.lang.String)
		 */
		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
		 */
		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#rollback()
		 */
		@Override
		public void rollback() throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#rollback(java.sql.Savepoint)
		 */
		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setAutoCommit(boolean)
		 */
		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setCatalog(java.lang.String)
		 */
		@Override
		public void setCatalog(String catalog) throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setClientInfo(java.util.Properties)
		 */
		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
		 */
		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setHoldability(int)
		 */
		@Override
		public void setHoldability(int holdability) throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setReadOnly(boolean)
		 */
		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setSavepoint()
		 */
		@Override
		public Savepoint setSavepoint() throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setSavepoint(java.lang.String)
		 */
		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			return null;
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setTransactionIsolation(int)
		 */
		@Override
		public void setTransactionIsolation(int level) throws SQLException {
		}

		/* (non-Javadoc)
		 * @see java.sql.Connection#setTypeMap(java.util.Map)
		 */
		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		}

	}
	
}
