/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import name.martingeisse.reporting.datasource.DataSources;

/**
 * Generic interface that is implemented by all document items. This interface
 * allows to "bind" items to data, which is the main point of generating
 * a report. Binding returns a document item of the same or a similar type.
 * Binding an item may also return the item itself, in case it is a "plain"
 * item whose content is independent of the data sources.
 */
public interface IDataBindable {

	/**
	 * Binds this object to the specified data sources.
	 * @param dataSources the data sources
	 * @return the bound object
	 */
	public Object bindToData(DataSources dataSources);
	
}
