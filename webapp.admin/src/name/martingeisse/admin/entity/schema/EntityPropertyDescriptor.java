/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import name.martingeisse.admin.entity.property.type.ISqlType;
import name.martingeisse.admin.entity.schema.autoform.EntityPropertyAutoformMetadata;

/**
 * This class describes a property of an entity, for example
 * a database column.
 */
public class EntityPropertyDescriptor {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the type
	 */
	private ISqlType type;

	/**
	 * the visibleInRawEntityList
	 */
	private boolean visibleInRawEntityList;

	/**
	 * the autoformMetadata
	 */
	private EntityPropertyAutoformMetadata autoformMetadata;

	/**
	 * Constructor.
	 */
	public EntityPropertyDescriptor() {
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public ISqlType getType() {
		return type;
	}

	/**
	 * Setter method for the type.
	 * @param type the type to set
	 */
	public void setType(final ISqlType type) {
		this.type = type;
	}

	/**
	 * Getter method for the visibleInRawEntityList.
	 * @return the visibleInRawEntityList
	 */
	public boolean isVisibleInRawEntityList() {
		return visibleInRawEntityList;
	}

	/**
	 * Setter method for the visibleInRawEntityList.
	 * @param visibleInRawEntityList the visibleInRawEntityList to set
	 */
	public void setVisibleInRawEntityList(final boolean visibleInRawEntityList) {
		this.visibleInRawEntityList = visibleInRawEntityList;
	}

	/**
	 * Getter method for the autoformMetadata.
	 * @return the autoformMetadata
	 */
	public EntityPropertyAutoformMetadata getAutoformMetadata() {
		return autoformMetadata;
	}

	/**
	 * Setter method for the autoformMetadata.
	 * @param autoformMetadata the autoformMetadata to set
	 */
	public void setAutoformMetadata(final EntityPropertyAutoformMetadata autoformMetadata) {
		this.autoformMetadata = autoformMetadata;
	}

}
