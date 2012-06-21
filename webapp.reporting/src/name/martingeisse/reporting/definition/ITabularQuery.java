/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition;

import java.sql.ResultSet;

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
	
}
