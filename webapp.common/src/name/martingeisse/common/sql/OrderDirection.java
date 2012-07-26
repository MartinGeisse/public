/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

/**
 * Specifies whether ascending or descending order is requested.
 */
public enum OrderDirection {

	/**
	 * the ASCENDING
	 */
	ASCENDING("ASC"),
	
	/**
	 * the DESCENDING
	 */
	DESCENDING("DESC");
	
	/**
	 * the sql
	 */
	private String sql;
	
	/**
	 * Constructor.
	 */
	private OrderDirection(String sql) {
		this.sql = sql;
	}
	
	/**
	 * Getter method for the sql.
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}
	
}
