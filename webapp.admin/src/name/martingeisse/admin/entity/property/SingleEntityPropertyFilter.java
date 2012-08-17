/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;

/**
 * This filter shows or hides a single entity property. The entity name
 * can be null to apply the filter to all entities.
 */
public class SingleEntityPropertyFilter extends AbstractFixedScoreEntityPropertyFilter {

	/**
	 * the entityName
	 */
	private String entityName;

	/**
	 * the propertyName
	 */
	private String propertyName;

	/**
	 * the visible
	 */
	private boolean visible;

	/**
	 * Constructor.
	 */
	public SingleEntityPropertyFilter() {
	}

	/**
	 * Constructor.
	 * @param score the score
	 * @param entityName the entity name
	 * @param propertyName the property name
	 * @param visible whether the property shall be visible or not
	 */
	public SingleEntityPropertyFilter(final int score, final String entityName, final String propertyName, final boolean visible) {
		super(score);
		this.entityName = entityName;
		this.propertyName = propertyName;
		this.visible = visible;
	}

	/**
	 * Getter method for the entityName.
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Setter method for the entityName.
	 * @param entityName the entityName to set
	 */
	public void setEntityName(final String entityName) {
		this.entityName = entityName;
	}

	/**
	 * Getter method for the propertyName.
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Setter method for the propertyName.
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
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

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityPropertyDisplayFilter#isPropertyVisible(name.martingeisse.admin.schema.EntityDescriptor, name.martingeisse.admin.schema.EntityPropertyDescriptor)
	 */
	@Override
	public Boolean isPropertyVisible(final EntityDescriptor entityDescriptor, final EntityPropertyDescriptor propertyDescriptor) {
		if (this.entityName != null && !this.entityName.equals(entityDescriptor.getName())) {
			return null;
		}
		if (!this.propertyName.equals(propertyDescriptor.getName())) {
			return null;
		}
		return visible;
	}

}
