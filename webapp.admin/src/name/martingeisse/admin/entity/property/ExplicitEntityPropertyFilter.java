/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.property;

import java.util.HashSet;
import java.util.Set;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;

/**
 * This filter applies to a single entity and explicitly keeps the
 * set of visible properties using a {@link Set} of property names.
 */
public class ExplicitEntityPropertyFilter extends AbstractFixedScoreEntityPropertyFilter {

	/**
	 * the entityName
	 */
	private String entityName;

	/**
	 * the visiblePropertyNames
	 */
	private Set<String> visiblePropertyNames;

	/**
	 * Constructor.
	 */
	public ExplicitEntityPropertyFilter() {
		visiblePropertyNames = new HashSet<String>();
	}

	/**
	 * Constructor.
	 * @param score the score
	 * @param entityName the entity name
	 */
	public ExplicitEntityPropertyFilter(final int score, final String entityName) {
		super(score);
		this.entityName = entityName;
		this.visiblePropertyNames = new HashSet<String>();
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
	 * Getter method for the visiblePropertyNames.
	 * @return the visiblePropertyNames
	 */
	public Set<String> getVisiblePropertyNames() {
		return visiblePropertyNames;
	}

	/**
	 * Setter method for the visiblePropertyNames.
	 * @param visiblePropertyNames the visiblePropertyNames to set
	 */
	public void setVisiblePropertyNames(final Set<String> visiblePropertyNames) {
		this.visiblePropertyNames = visiblePropertyNames;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.capabilities.IRawEntityListPropertyDisplayFilter#isPropertyVisible(name.martingeisse.admin.schema.EntityDescriptor, name.martingeisse.admin.schema.EntityPropertyDescriptor)
	 */
	@Override
	public Boolean isPropertyVisible(final EntityDescriptor entityDescriptor, final EntityPropertyDescriptor propertyDescriptor) {
		if (!this.entityName.equals(entityDescriptor.getName())) {
			return null;
		} else {
			return visiblePropertyNames.contains(propertyDescriptor.getName());
		}
	}

}
