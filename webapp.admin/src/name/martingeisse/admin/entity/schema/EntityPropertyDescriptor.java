/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;


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
	 * the visibleInRawEntityList
	 */
	private boolean visibleInRawEntityList;

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

}
