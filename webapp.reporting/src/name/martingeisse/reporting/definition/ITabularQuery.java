/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition;

import java.sql.ResultSet;
import java.sql.Statement;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.document.IDataBindable;

/**
 * This bindable returns tabular data from the data source.
 */
public interface ITabularQuery extends IDataBindable {

	/**
	 * This interface specializes the return type to a
	 * JDBC {@link ResultSet}.
	 */
	@Override
	public ResultSet bindToData(DataSources dataSources);
	
	/**
	 * Used by callers of bindToData() to know whether they must close
	 * the {@link Statement} of the {@link ResultSet} when they are finished.
	 * 
	 * TODO: Clients do not actually do that yet, it's hard for clients
	 * that deal with the more abstract {@link IDataBindable}, and it doesn't
	 * really do any harm to do so except temporarily use more resources in
	 * this program as well as the SQL server. -- Maybe provide a generic
	 * close(Object) in IDataBindable that cleans up the bound object. Generic
	 * implementations have to pass this method on to their child objects,
	 * specific ones like JDBC can actually clean up. This also removes the
	 * ugly if statement on the return value of this method in clients of
	 * this interface. 
	 *  
	 * @return true if the caller of bindToData() must close the statement, false if
	 * not (e.g. if the statement is shared)
	 */
	public boolean isStatementMustBeClosed();
	
}
