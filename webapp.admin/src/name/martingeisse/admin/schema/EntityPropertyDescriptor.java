/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.schema;

import java.io.Serializable;

/**
 * This class describes a property of an entity, for example
 * a database column.
 * 
 * TODO: this class should not be serializable. Instead, models should be able
 * to detach and re-attach from/to instances of this class.
 */
public class EntityPropertyDescriptor implements Serializable {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the visible
	 */
	private boolean visible;

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
	 * Getter method for the visible.
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Setter method for the visible.
	 * @param visible the visible to set
	 */
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

}
