/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.entity.schema.ApplicationSchema;

/**
 * Detects an entity reference whenever a property name matches a fixed
 * string, with a fixed destination entity. The detector can optionally
 * be restricted to a single referencing (source) entity.
 */
public final class FixedNameEntityReferenceDetector extends AbstractEntityReferenceDetector {

	/**
	 * the sourceEntityName
	 */
	private final String sourceEntityName;

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
	 * @param sourceEntityName the name of the referencing entity, or null to match any referencing entity
	 * @param knownPropertyName the "known" property name that marks references
	 * @param destinationEntityName the name of the destination entity
	 */
	public FixedNameEntityReferenceDetector(final String sourceEntityName, final String knownPropertyName, final String destinationEntityName) {
		this.sourceEntityName = sourceEntityName;
		this.knownPropertyName = knownPropertyName;
		this.destinationEntityName = destinationEntityName;
	}

	/**
	 * Getter method for the sourceEntityName.
	 * @return the sourceEntityName
	 */
	public String getSourceEntityName() {
		return sourceEntityName;
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
	 * @see name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.entity.schema.ApplicationSchema, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String detectEntityReference(final ApplicationSchema schema, final String entityName, final String entityTableName, final String propertyName) {
		if (sourceEntityName == null || sourceEntityName.equals(entityName)) {
			if (propertyName.equals(knownPropertyName)) {
				return destinationEntityName;
			}
		}
		return null;
	}

}
