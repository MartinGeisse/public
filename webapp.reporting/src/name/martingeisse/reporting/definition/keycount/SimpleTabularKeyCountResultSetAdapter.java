/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.keycount;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class adapts a JDBC {@link ResultSet} to {@link IKeyCountResultSet}
 * behavior. It expects the key in the first column and the count in the second.
 * This class does not actually count anything; this is expected to happen
 * as part of the wrapped query.
 */
public class SimpleTabularKeyCountResultSetAdapter implements IKeyCountResultSet {

	/**
	 * the resultSet
	 */
	private final ResultSet resultSet;
	
	/**
	 * Constructor.
	 * @param resultSet the result set to wrap
	 */
	public SimpleTabularKeyCountResultSetAdapter(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.keycount.IKeyCountResultSet#next()
	 */
	@Override
	public boolean next() {
		try {
			return resultSet.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.keycount.IKeyCountResultSet#get()
	 */
	@Override
	public KeyCountEntry get() {
		try {
			String key = resultSet.getString(1);
			long count = resultSet.getLong(2);
			return new KeyCountEntry(key, count);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.definition.keycount.IKeyCountResultSet#close()
	 */
	@Override
	public void close() {
		try {
			resultSet.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
