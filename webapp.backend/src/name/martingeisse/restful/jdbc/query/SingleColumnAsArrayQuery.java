/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.jdbc.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This query fetches the value of a single column from a number of
 * rows and returns the results from all rows in an array.
 */
public class SingleColumnAsArrayQuery extends AbstractReductionQuery<Object[]> {

	/**
	 * the query
	 */
	private String query;
	
	/**
	 * Constructor.
	 * @param query the query to execute
	 */
	public SingleColumnAsArrayQuery(String query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.jdbc.query.AbstractReductionQuery#execute(java.sql.Connection, java.sql.Statement)
	 */
	@Override
	protected Object[] execute(Connection connection, Statement statement) throws SQLException {
		ResultSet resultSet = statement.executeQuery(query);
		try {
			List<Object> resultList = new ArrayList<Object>();
			while (resultSet.next()) {
				resultList.add(resultSet.getObject(1));
			}
			return resultList.toArray();
		} finally {
			resultSet.close();
		}
	}
	
	/**
	 * Executes a query of this type and returns the results.
	 * @param queryString the query to execute
	 * @return the result array
	 */
	public static Object[] execute(String queryString) {
		return new SingleColumnAsArrayQuery(queryString).execute();
	}
	
}
