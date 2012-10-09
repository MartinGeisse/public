/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.datarow.IDataRow;

/**
 * Specialization of {@link IDataRow} for entities, i.e. data rows that
 * correspond exactly to records in the database.
 */
public interface IEntityInstance extends IDataRow {

	/**
	 * Getter method for the entityName.
	 * @return the entityName
	 */
	public String getEntityName();

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity();

	/**
	 * Getter method for the entity id.
	 * @return the entity id
	 */
	public Object getEntityId();

}
