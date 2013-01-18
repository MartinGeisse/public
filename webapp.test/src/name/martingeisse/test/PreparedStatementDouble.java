/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.test;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.lang.NotImplementedException;

/**
 * Base class for {@link PreparedStatement} doubles.
 */
public class PreparedStatementDouble implements PreparedStatement {

	/* (non-Javadoc)
	 * @see java.sql.Statement#addBatch(java.lang.String)
	 */
	@Override
	public void addBatch(final String arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#cancel()
	 */
	@Override
	public void cancel() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#clearBatch()
	 */
	@Override
	public void clearBatch() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#close()
	 */
	@Override
	public void close() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int)
	 */
	@Override
	public boolean execute(final String arg0, final int arg1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, int[])
	 */
	@Override
	public boolean execute(final String arg0, final int[] arg1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean execute(final String arg0, final String[] arg1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#execute(java.lang.String)
	 */
	@Override
	public boolean execute(final String arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeBatch()
	 */
	@Override
	public int[] executeBatch() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(final String arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int)
	 */
	@Override
	public int executeUpdate(final String arg0, final int arg1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
	 */
	@Override
	public int executeUpdate(final String arg0, final int[] arg1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
	 */
	@Override
	public int executeUpdate(final String arg0, final String[] arg1) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#executeUpdate(java.lang.String)
	 */
	@Override
	public int executeUpdate(final String arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getGeneratedKeys()
	 */
	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxFieldSize()
	 */
	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMaxRows()
	 */
	@Override
	public int getMaxRows() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults()
	 */
	@Override
	public boolean getMoreResults() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getMoreResults(int)
	 */
	@Override
	public boolean getMoreResults(final int arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getQueryTimeout()
	 */
	@Override
	public int getQueryTimeout() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSet()
	 */
	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetConcurrency()
	 */
	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetHoldability()
	 */
	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getResultSetType()
	 */
	@Override
	public int getResultSetType() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getUpdateCount()
	 */
	@Override
	public int getUpdateCount() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#isPoolable()
	 */
	@Override
	public boolean isPoolable() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setCursorName(java.lang.String)
	 */
	@Override
	public void setCursorName(final String arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setEscapeProcessing(boolean)
	 */
	@Override
	public void setEscapeProcessing(final boolean arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(final int arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(final int arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxFieldSize(int)
	 */
	@Override
	public void setMaxFieldSize(final int arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setMaxRows(int)
	 */
	@Override
	public void setMaxRows(final int arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setPoolable(boolean)
	 */
	@Override
	public void setPoolable(final boolean arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Statement#setQueryTimeout(int)
	 */
	@Override
	public void setQueryTimeout(final int arg0) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#addBatch()
	 */
	@Override
	public void addBatch() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#clearParameters()
	 */
	@Override
	public void clearParameters() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#execute()
	 */
	@Override
	public boolean execute() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#executeQuery()
	 */
	@Override
	public ResultSet executeQuery() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#executeUpdate()
	 */
	@Override
	public int executeUpdate() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#getMetaData()
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#getParameterMetaData()
	 */
	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setArray(int, java.sql.Array)
	 */
	@Override
	public void setArray(final int parameterIndex, final Array x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, int)
	 */
	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream, long)
	 */
	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, int)
	 */
	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream, long)
	 */
	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBlob(int, java.sql.Blob)
	 */
	@Override
	public void setBlob(final int parameterIndex, final Blob x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBlob(int, java.io.InputStream)
	 */
	@Override
	public void setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	@Override
	public void setBoolean(final int parameterIndex, final boolean x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setByte(int, byte)
	 */
	@Override
	public void setByte(final int parameterIndex, final byte x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setBytes(int, byte[])
	 */
	@Override
	public void setBytes(final int parameterIndex, final byte[] x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, int)
	 */
	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setClob(int, java.sql.Clob)
	 */
	@Override
	public void setClob(final int parameterIndex, final Clob x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader, long)
	 */
	@Override
	public void setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setClob(int, java.io.Reader)
	 */
	@Override
	public void setClob(final int parameterIndex, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, java.util.Calendar)
	 */
	@Override
	public void setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setDate(int, java.sql.Date)
	 */
	@Override
	public void setDate(final int parameterIndex, final Date x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setDouble(int, double)
	 */
	@Override
	public void setDouble(final int parameterIndex, final double x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setFloat(int, float)
	 */
	@Override
	public void setFloat(final int parameterIndex, final float x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setInt(int, int)
	 */
	@Override
	public void setInt(final int parameterIndex, final int x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setLong(int, long)
	 */
	@Override
	public void setLong(final int parameterIndex, final long x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNClob(int, java.sql.NClob)
	 */
	@Override
	public void setNClob(final int parameterIndex, final NClob value) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader, long)
	 */
	@Override
	public void setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNClob(int, java.io.Reader)
	 */
	@Override
	public void setNClob(final int parameterIndex, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNString(int, java.lang.String)
	 */
	@Override
	public void setNString(final int parameterIndex, final String value) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	@Override
	public void setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	@Override
	public void setNull(final int parameterIndex, final int sqlType) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int, int)
	 */
	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object, int)
	 */
	@Override
	public void setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setObject(int, java.lang.Object)
	 */
	@Override
	public void setObject(final int parameterIndex, final Object x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setRef(int, java.sql.Ref)
	 */
	@Override
	public void setRef(final int parameterIndex, final Ref x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setRowId(int, java.sql.RowId)
	 */
	@Override
	public void setRowId(final int parameterIndex, final RowId x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setShort(int, short)
	 */
	@Override
	public void setShort(final int parameterIndex, final short x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setString(int, java.lang.String)
	 */
	@Override
	public void setString(final int parameterIndex, final String x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, java.util.Calendar)
	 */
	@Override
	public void setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTime(int, java.sql.Time)
	 */
	@Override
	public void setTime(final int parameterIndex, final Time x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, java.util.Calendar)
	 */
	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setURL(int, java.net.URL)
	 */
	@Override
	public void setURL(final int parameterIndex, final URL x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.PreparedStatement#setUnicodeStream(int, java.io.InputStream, int)
	 */
	@Override
	@Deprecated
	public void setUnicodeStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

}
