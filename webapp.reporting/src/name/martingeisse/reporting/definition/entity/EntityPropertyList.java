/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

import java.util.ArrayList;

/**
 * This class keeps a list of {@link EntityProperty} objects and stores
 * the properly list specification from the report definition.
 */
public final class EntityPropertyList extends ArrayList<EntityProperty> {

	/**
	 * the query
	 */
	private final EntityQuery query;
	
	/**
	 * Constructor.
	 * @param query the query
	 */
	EntityPropertyList(EntityQuery query) {
		this.query = query;
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public EntityQuery getQuery() {
		return query;
	}

	/**
	 * 
	 */
	String buildColumnSpecification() {
		
		// handle the no-column case
		if (isEmpty()) {
			return "0 AS dummy";
		}
		
		// handle the normal case
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (EntityProperty property : this) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(property.getPathText());
		}
		return builder.toString();
		
	}
	
}
