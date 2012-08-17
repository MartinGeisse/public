/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.populator;

import name.martingeisse.admin.entity.instance.EntityInstance;

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
