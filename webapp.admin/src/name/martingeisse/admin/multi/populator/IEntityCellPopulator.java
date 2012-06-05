/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi.populator;

import name.martingeisse.admin.single.EntityInstance;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;

/**
 * Specialized cell populator for entity tables.
 */
public interface IEntityCellPopulator extends ICellPopulator<EntityInstance> {

	/**
	 * @return the column title
	 */
	public String getTitle();
	
}
