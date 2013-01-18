/**
 * Copyright (c) 2011 Martin Geisse
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
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import name.martingeisse.common.util.NotImplementedException;

/**
 * Base class for {@link ResultSet} doubles.
 */
public class ResultSetDouble implements ResultSet {

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
	 * @see java.sql.ResultSet#absolute(int)
	 */
	@Override
	public boolean absolute(final int row) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#afterLast()
	 */
	@Override
	public void afterLast() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#beforeFirst()
	 */
	@Override
	public void beforeFirst() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#cancelRowUpdates()
	 */
	@Override
	public void cancelRowUpdates() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#clearWarnings()
	 */
	@Override
	public void clearWarnings() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#close()
	 */
	@Override
	public void close() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#deleteRow()
	 */
	@Override
	public void deleteRow() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#findColumn(java.lang.String)
	 */
	@Override
	public int findColumn(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#first()
	 */
	@Override
	public boolean first() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getArray(int)
	 */
	@Override
	public Array getArray(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getArray(java.lang.String)
	 */
	@Override
	public Array getArray(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getAsciiStream(int)
	 */
	@Override
	public InputStream getAsciiStream(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getAsciiStream(java.lang.String)
	 */
	@Override
	public InputStream getAsciiStream(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBigDecimal(int, int)
	 */
	@Override
	@Deprecated
	public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBigDecimal(int)
	 */
	@Override
	public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String, int)
	 */
	@Override
	@Deprecated
	public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBigDecimal(java.lang.String)
	 */
	@Override
	public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBinaryStream(int)
	 */
	@Override
	public InputStream getBinaryStream(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBinaryStream(java.lang.String)
	 */
	@Override
	public InputStream getBinaryStream(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBlob(int)
	 */
	@Override
	public Blob getBlob(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBlob(java.lang.String)
	 */
	@Override
	public Blob getBlob(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	@Override
	public boolean getBoolean(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getByte(int)
	 */
	@Override
	public byte getByte(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getByte(java.lang.String)
	 */
	@Override
	public byte getByte(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBytes(int)
	 */
	@Override
	public byte[] getBytes(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getBytes(java.lang.String)
	 */
	@Override
	public byte[] getBytes(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getCharacterStream(int)
	 */
	@Override
	public Reader getCharacterStream(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getCharacterStream(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getClob(int)
	 */
	@Override
	public Clob getClob(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getClob(java.lang.String)
	 */
	@Override
	public Clob getClob(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getConcurrency()
	 */
	@Override
	public int getConcurrency() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getCursorName()
	 */
	@Override
	public String getCursorName() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getDate(int, java.util.Calendar)
	 */
	@Override
	public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getDate(int)
	 */
	@Override
	public Date getDate(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getDate(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	@Override
	public double getDouble(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getFetchDirection()
	 */
	@Override
	public int getFetchDirection() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getFetchSize()
	 */
	@Override
	public int getFetchSize() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getFloat(int)
	 */
	@Override
	public float getFloat(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getHoldability()
	 */
	@Override
	public int getHoldability() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getInt(int)
	 */
	@Override
	public int getInt(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	@Override
	public int getInt(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getLong(int)
	 */
	@Override
	public long getLong(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	@Override
	public long getLong(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getMetaData()
	 */
	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getNCharacterStream(int)
	 */
	@Override
	public Reader getNCharacterStream(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getNCharacterStream(java.lang.String)
	 */
	@Override
	public Reader getNCharacterStream(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getNClob(int)
	 */
	@Override
	public NClob getNClob(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getNClob(java.lang.String)
	 */
	@Override
	public NClob getNClob(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getNString(int)
	 */
	@Override
	public String getNString(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getNString(java.lang.String)
	 */
	@Override
	public String getNString(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getObject(int, java.util.Map)
	 */
	@Override
	public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getObject(int)
	 */
	@Override
	public Object getObject(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getObject(java.lang.String, java.util.Map)
	 */
	@Override
	public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getRef(int)
	 */
	@Override
	public Ref getRef(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getRef(java.lang.String)
	 */
	@Override
	public Ref getRef(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getRow()
	 */
	@Override
	public int getRow() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getRowId(int)
	 */
	@Override
	public RowId getRowId(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getRowId(java.lang.String)
	 */
	@Override
	public RowId getRowId(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getSQLXML(int)
	 */
	@Override
	public SQLXML getSQLXML(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getSQLXML(java.lang.String)
	 */
	@Override
	public SQLXML getSQLXML(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getShort(int)
	 */
	@Override
	public short getShort(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getShort(java.lang.String)
	 */
	@Override
	public short getShort(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getStatement()
	 */
	@Override
	public Statement getStatement() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getString(int)
	 */
	@Override
	public String getString(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	@Override
	public String getString(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTime(int, java.util.Calendar)
	 */
	@Override
	public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTime(int)
	 */
	@Override
	public Time getTime(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTime(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	@Override
	public Time getTime(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTimestamp(int, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	@Override
	public Timestamp getTimestamp(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String, java.util.Calendar)
	 */
	@Override
	public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	@Override
	public Timestamp getTimestamp(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getType()
	 */
	@Override
	public int getType() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getURL(int)
	 */
	@Override
	public URL getURL(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getURL(java.lang.String)
	 */
	@Override
	public URL getURL(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getUnicodeStream(int)
	 */
	@Override
	@Deprecated
	public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getUnicodeStream(java.lang.String)
	 */
	@Override
	@Deprecated
	public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#getWarnings()
	 */
	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#insertRow()
	 */
	@Override
	public void insertRow() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#isAfterLast()
	 */
	@Override
	public boolean isAfterLast() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#isBeforeFirst()
	 */
	@Override
	public boolean isBeforeFirst() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#isClosed()
	 */
	@Override
	public boolean isClosed() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#isFirst()
	 */
	@Override
	public boolean isFirst() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#isLast()
	 */
	@Override
	public boolean isLast() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#last()
	 */
	@Override
	public boolean last() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#moveToCurrentRow()
	 */
	@Override
	public void moveToCurrentRow() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#moveToInsertRow()
	 */
	@Override
	public void moveToInsertRow() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#next()
	 */
	@Override
	public boolean next() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#previous()
	 */
	@Override
	public boolean previous() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#refreshRow()
	 */
	@Override
	public void refreshRow() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#relative(int)
	 */
	@Override
	public boolean relative(final int rows) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#rowDeleted()
	 */
	@Override
	public boolean rowDeleted() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#rowInserted()
	 */
	@Override
	public boolean rowInserted() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#rowUpdated()
	 */
	@Override
	public boolean rowUpdated() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#setFetchDirection(int)
	 */
	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#setFetchSize(int)
	 */
	@Override
	public void setFetchSize(final int rows) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateArray(int, java.sql.Array)
	 */
	@Override
	public void updateArray(final int columnIndex, final Array x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateArray(java.lang.String, java.sql.Array)
	 */
	@Override
	public void updateArray(final String columnLabel, final Array x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream, long)
	 */
	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateAsciiStream(int, java.io.InputStream)
	 */
	@Override
	public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, int)
	 */
	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream, long)
	 */
	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateAsciiStream(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBigDecimal(int, java.math.BigDecimal)
	 */
	@Override
	public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBigDecimal(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, int)
	 */
	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream, long)
	 */
	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBinaryStream(int, java.io.InputStream)
	 */
	@Override
	public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, int)
	 */
	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream, long)
	 */
	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBinaryStream(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBlob(int, java.sql.Blob)
	 */
	@Override
	public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream, long)
	 */
	@Override
	public void updateBlob(final int columnIndex, final InputStream inputStream, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBlob(int, java.io.InputStream)
	 */
	@Override
	public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.sql.Blob)
	 */
	@Override
	public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream, long)
	 */
	@Override
	public void updateBlob(final String columnLabel, final InputStream inputStream, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBlob(java.lang.String, java.io.InputStream)
	 */
	@Override
	public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBoolean(int, boolean)
	 */
	@Override
	public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBoolean(java.lang.String, boolean)
	 */
	@Override
	public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateByte(int, byte)
	 */
	@Override
	public void updateByte(final int columnIndex, final byte x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateByte(java.lang.String, byte)
	 */
	@Override
	public void updateByte(final String columnLabel, final byte x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBytes(int, byte[])
	 */
	@Override
	public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateBytes(java.lang.String, byte[])
	 */
	@Override
	public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, int)
	 */
	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, int)
	 */
	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader, final int length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateCharacterStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateClob(int, java.sql.Clob)
	 */
	@Override
	public void updateClob(final int columnIndex, final Clob x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateClob(int, java.io.Reader)
	 */
	@Override
	public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.sql.Clob)
	 */
	@Override
	public void updateClob(final String columnLabel, final Clob x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateDate(int, java.sql.Date)
	 */
	@Override
	public void updateDate(final int columnIndex, final Date x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateDate(java.lang.String, java.sql.Date)
	 */
	@Override
	public void updateDate(final String columnLabel, final Date x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateDouble(int, double)
	 */
	@Override
	public void updateDouble(final int columnIndex, final double x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateDouble(java.lang.String, double)
	 */
	@Override
	public void updateDouble(final String columnLabel, final double x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateFloat(int, float)
	 */
	@Override
	public void updateFloat(final int columnIndex, final float x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateFloat(java.lang.String, float)
	 */
	@Override
	public void updateFloat(final String columnLabel, final float x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateInt(int, int)
	 */
	@Override
	public void updateInt(final int columnIndex, final int x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateInt(java.lang.String, int)
	 */
	@Override
	public void updateInt(final String columnLabel, final int x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateLong(int, long)
	 */
	@Override
	public void updateLong(final int columnIndex, final long x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateLong(java.lang.String, long)
	 */
	@Override
	public void updateLong(final String columnLabel, final long x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader, long)
	 */
	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNCharacterStream(int, java.io.Reader)
	 */
	@Override
	public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNCharacterStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNClob(int, java.sql.NClob)
	 */
	@Override
	public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader, long)
	 */
	@Override
	public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNClob(int, java.io.Reader)
	 */
	@Override
	public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.sql.NClob)
	 */
	@Override
	public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader, long)
	 */
	@Override
	public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNClob(java.lang.String, java.io.Reader)
	 */
	@Override
	public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNString(int, java.lang.String)
	 */
	@Override
	public void updateNString(final int columnIndex, final String nString) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNString(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateNString(final String columnLabel, final String nString) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNull(int)
	 */
	@Override
	public void updateNull(final int columnIndex) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateNull(java.lang.String)
	 */
	@Override
	public void updateNull(final String columnLabel) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object, int)
	 */
	@Override
	public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateObject(int, java.lang.Object)
	 */
	@Override
	public void updateObject(final int columnIndex, final Object x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object, int)
	 */
	@Override
	public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public void updateObject(final String columnLabel, final Object x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateRef(int, java.sql.Ref)
	 */
	@Override
	public void updateRef(final int columnIndex, final Ref x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateRef(java.lang.String, java.sql.Ref)
	 */
	@Override
	public void updateRef(final String columnLabel, final Ref x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateRow()
	 */
	@Override
	public void updateRow() throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateRowId(int, java.sql.RowId)
	 */
	@Override
	public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateRowId(java.lang.String, java.sql.RowId)
	 */
	@Override
	public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateSQLXML(int, java.sql.SQLXML)
	 */
	@Override
	public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateSQLXML(java.lang.String, java.sql.SQLXML)
	 */
	@Override
	public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateShort(int, short)
	 */
	@Override
	public void updateShort(final int columnIndex, final short x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateShort(java.lang.String, short)
	 */
	@Override
	public void updateShort(final String columnLabel, final short x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateString(int, java.lang.String)
	 */
	@Override
	public void updateString(final int columnIndex, final String x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateString(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateString(final String columnLabel, final String x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateTime(int, java.sql.Time)
	 */
	@Override
	public void updateTime(final int columnIndex, final Time x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateTime(java.lang.String, java.sql.Time)
	 */
	@Override
	public void updateTime(final String columnLabel, final Time x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateTimestamp(int, java.sql.Timestamp)
	 */
	@Override
	public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#updateTimestamp(java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see java.sql.ResultSet#wasNull()
	 */
	@Override
	public boolean wasNull() throws SQLException {
		throw new NotImplementedException();
	}

}
