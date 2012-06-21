/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.keycount;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.document.IDataBindable;

/**
 * This query returns a sequence of name/count pairs.
 */
public interface IKeyCountQuery extends IDataBindable {

	/**
	 * This interface specializes the return type to an {@link IKeyCountResultSet}.
	 */
	@Override
	public IKeyCountResultSet bindToData(DataSources dataSources);
	
}
