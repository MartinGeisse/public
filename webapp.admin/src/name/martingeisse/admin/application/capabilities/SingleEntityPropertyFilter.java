/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;

/**
 * This filter shows or hides a single entity property.
 */
public class SingleEntityPropertyFilter implements IEntityPropertyDisplayFilter, IPlugin {

	/**
	 * the score
	 */
	private int score;

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
		this.score = score;
		this.entityName = entityName;
		this.propertyName = propertyName;
		this.visible = visible;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityPropertyDisplayFilter#getScore()
	 */
	@Override
	public int getScore() {
		return score;
	}

	/**
	 * Setter method for the score.
	 * @param score the score to set
	 */
	public void setScore(final int score) {
		this.score = score;
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
	 * @see name.martingeisse.admin.application.IPlugin#contribute(name.martingeisse.admin.application.capabilities.ApplicationCapabilities)
	 */
	@Override
	public void contribute(final ApplicationCapabilities applicationCapabilities) {
		applicationCapabilities.getEntityPropertyDisplayFilters().add(this);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IEntityPropertyDisplayFilter#isPropertyVisible(name.martingeisse.admin.schema.EntityDescriptor, name.martingeisse.admin.schema.EntityPropertyDescriptor)
	 */
	@Override
	public Boolean isPropertyVisible(final EntityDescriptor entityDescriptor, final EntityPropertyDescriptor propertyDescriptor) {
		if (entityDescriptor.getTableName().equals(entityName) && propertyDescriptor.getName().equals(propertyName)) {
			return visible;
		} else {
			return null;
		}
	}

}
