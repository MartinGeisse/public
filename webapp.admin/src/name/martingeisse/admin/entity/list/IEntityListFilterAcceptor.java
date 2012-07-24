/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;


/**
 * This interface is implemented by objects that are able
 * to make use of an entity list filter.
 */
public interface IEntityListFilterAcceptor {

	/**
	 * Sets the entity list filter for this consumer.
	 * @param filter the entity list filter
	 */
	public void acceptEntityListFilter(IEntityListFilter filter);
	
}
