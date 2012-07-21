/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;


/**
 * This interface is implemented by objects that are (possibly) able
 * to return an entity list filter.
 */
public interface IEntityListFilterProvider {

	/**
	 * Returns the entity list filter from this provider.
	 * @return the entity list filter
	 */
	public IEntityListFilter getEntityListFilter();
	
}
