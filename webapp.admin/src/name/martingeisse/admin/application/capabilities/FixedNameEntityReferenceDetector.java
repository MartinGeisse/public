/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.schema.AbstractApplicationSchema;

/**
 * Detects an entity reference whenever a property name matches a fixed
 * string, with a fixed destination entity.
 */
public class FixedNameEntityReferenceDetector implements IEntityReferenceDetector {

	/**
	 * the name
	 */
	private final String knownPropertyName;

	/**
	 * the destinationEntityName
	 */
	private final String destinationEntityName;

	/**
	 * Constructor.
	 * @param knownPropertyName the "known" property name that marks references
	 * @param destinationEntityName the name of the destination entity
	 */
	public FixedNameEntityReferenceDetector(final String knownPropertyName, final String destinationEntityName) {
		this.knownPropertyName = knownPropertyName;
		this.destinationEntityName = destinationEntityName;
	}

	/**
	 * Getter method for the knownPropertyName.
	 * @return the knownPropertyName
	 */
	public String getKnownPropertyName() {
		return knownPropertyName;
	}

	/**
	 * Getter method for the destinationEntityName.
	 * @return the destinationEntityName
	 */
	public String getDestinationEntityName() {
		return destinationEntityName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.schema.AbstractApplicationSchema, java.lang.String, java.lang.String)
	 */
	@Override
	public String detectEntityReference(AbstractApplicationSchema schema, String entityName, String propertyName) {
		if (propertyName.equals(knownPropertyName)) {
			return destinationEntityName;
		} else {
			return null;
		}
	}

}
