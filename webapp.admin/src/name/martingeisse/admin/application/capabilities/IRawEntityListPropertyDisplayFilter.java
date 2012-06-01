/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;

/**
 * Implementations decide whether an entity property is visible
 * to the user. For each property, the filter with the highest
 * score that defines visibility for that property wins.
 * Properties are visible by default, i.e. if no filter affects
 * a property, then it is visible.
 * 
 * 
 */
public interface IRawEntityListPropertyDisplayFilter {

	/**
	 * @param entityDescriptor the entity descriptor
	 * @param propertyDescriptor the property descriptor
	 * @return the score for this filter, used for priority
	 */
	public int getScore(EntityDescriptor entityDescriptor, EntityPropertyDescriptor propertyDescriptor);

	/**
	 * Checks whether the specified property is visible
	 * @param entityDescriptor the entity descriptor
	 * @param propertyDescriptor the property descriptor
	 * @return true if visible, false if not, null if this filter does
	 * not affect that property
	 */
	public Boolean isPropertyVisible(EntityDescriptor entityDescriptor, EntityPropertyDescriptor propertyDescriptor);
	
}
