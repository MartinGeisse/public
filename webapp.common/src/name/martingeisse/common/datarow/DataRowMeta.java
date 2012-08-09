/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Describes the data stored in a {@link DataRow}.
 */
public final class DataRowMeta implements Serializable {

	/**
	 * the names
	 */
	private String[] names;

	/**
	 * Constructor.
	 */
	public DataRowMeta() {
	}

	/**
	 * Constructor.
	 * @param resultSetMetaData the JDBC meta-data to create this object from
	 * @throws SQLException on SQL errors
	 */
	public DataRowMeta(ResultSetMetaData resultSetMetaData) throws SQLException {
		names = new String[resultSetMetaData.getColumnCount()];
		for (int i=0; i<names.length; i++) {
			names[i] = resultSetMetaData.getColumnLabel(i + 1);
		}
	}

	/**
	 * Getter method for the names.
	 * @return the names
	 */
	public String[] getNames() {
		return names;
	}

	/**
	 * Setter method for the names.
	 * @param names the names to set
	 */
	public void setNames(final String[] names) {
		this.names = names;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object untypedOther) {
		if (untypedOther instanceof DataRowMeta) {
			DataRowMeta other = (DataRowMeta)untypedOther;
			return Arrays.equals(names, other.getNames());
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(names);
	}
	
}
