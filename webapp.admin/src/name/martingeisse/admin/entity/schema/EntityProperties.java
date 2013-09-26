/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class encapsulates the properties for an entity.
 */
public final class EntityProperties implements Iterable<EntityPropertyDescriptor> {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;
	
	/**
	 * the propertiesInDatabaseOrder
	 */
	private final List<EntityPropertyDescriptor> propertiesInDatabaseOrder;
	
	/**
	 * the propertiesByName
	 */
	private final Map<String, EntityPropertyDescriptor> propertiesByName;
	
	/**
	 * Constructor.
	 * @param entity the entity
	 * @param propertiesInDatabaseOrder the list of properties in database order
	 */
	EntityProperties(EntityDescriptor entity, List<EntityPropertyDescriptor> propertiesInDatabaseOrder) {
		this.entity = entity;
		this.propertiesInDatabaseOrder = propertiesInDatabaseOrder;
		this.propertiesByName = new HashMap<String, EntityPropertyDescriptor>();
		for (final EntityPropertyDescriptor propertyDescriptor : propertiesInDatabaseOrder) {
			propertiesByName.put(propertyDescriptor.getName(), propertyDescriptor);
		}
	}
	
	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}

	/**
	 * Getter method for the propertiesInDatabaseOrder.
	 * @return the propertiesInDatabaseOrder
	 */
	public List<EntityPropertyDescriptor> getPropertiesInDatabaseOrder() {
		return propertiesInDatabaseOrder;
	}
	
	/**
	 * Getter method for the propertiesByName.
	 * @return the propertiesByName
	 */
	public Map<String, EntityPropertyDescriptor> getPropertiesByName() {
		return propertiesByName;
	}
	
	/**
	 * Returns a property by name.
	 * @param name the name
	 * @return the property
	 */
	public EntityPropertyDescriptor get(String name) {
		return propertiesByName.get(name);
	}

	/**
	 * @return the names of the fields for raw lists of this entity type, in the order
	 * they shall be displayed.
	 */
	public String[] getRawEntityListPropertyOrder() {

		// determine the list of visible fields
		final List<EntityPropertyDescriptor> fieldOrder = new ArrayList<EntityPropertyDescriptor>();
		for (final EntityPropertyDescriptor property : propertiesInDatabaseOrder) {
			if (property.isVisibleInRawEntityList()) {
				fieldOrder.add(property);
			}
		}

		// build an array of the field names
		final String[] fieldOrderArray = new String[fieldOrder.size()];
		int position = 0;
		for (final EntityPropertyDescriptor property : fieldOrder) {
			fieldOrderArray[position] = property.getName();
			position++;
		}

		return fieldOrderArray;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<EntityPropertyDescriptor> iterator() {
		return propertiesInDatabaseOrder.iterator();
	}
	
}
