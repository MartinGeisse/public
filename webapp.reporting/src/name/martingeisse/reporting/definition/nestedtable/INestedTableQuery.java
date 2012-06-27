/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.nestedtable;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.document.IDataBindable;

/**
 * This query returns an {@link INestedTableResult} when bound to the data
 * sources.
 */
public interface INestedTableQuery extends IDataBindable {

	/**
	 * This interface specializes the return type to an {@link INestedTableResult}.
	 */
	@Override
	public INestedTableResult bindToData(DataSources dataSources);

}
