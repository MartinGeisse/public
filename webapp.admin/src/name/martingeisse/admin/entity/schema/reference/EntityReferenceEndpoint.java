/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Information about a reference between entities. For any property of an
 * entity that corresponds to a property of the same or another entity,
 * a pair of instances of this class is created by the
 * {@link IEntityReferenceDetector} and stored in the application schema.
 * No additional per-entity-instance objects are used to describe
 * references.
 * 
 * Each endpoint is defined by an entity, entity property, and multiplicity.
 * Entity references are undirected, that is, other than these three
 * defining characteristics of a reference, its two endpoints are not
 * distinguished.
 */
public final class EntityReferenceEndpoint {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;

	/**
	 * the propertyName
	 */
	private final String propertyName;

	/**
	 * the multiplicity
	 */
	private final EntityReferenceEndpointMultiplicity multiplicity;

	/**
	 * the other
	 */
	private final EntityReferenceEndpoint other;

	/**
	 * Creates and returns a pair of endpoints. The two endpoints are called
	 * "near" and "far" by this method only to define the meaning of the
	 * method parameters and return value.
	 * 
	 * @param nearEntity the entity used by the near endpoint
	 * @param nearPropertyName the name of the key property used by the near endpoint
	 * @param nearMultiplicity the multiplicity of the reference at the near endpoint
	 * @param farEntity the entity used by the far endpoint
	 * @param farPropertyName the name of the key property used by the far endpoint
	 * @param farMultiplicity the multiplicity of the reference at the far endpoint
	 * 
	 * @return the near endpoint.
	 */
	public static EntityReferenceEndpoint createPair(final EntityDescriptor nearEntity, final String nearPropertyName, final EntityReferenceEndpointMultiplicity nearMultiplicity, final EntityDescriptor farEntity, final String farPropertyName, final EntityReferenceEndpointMultiplicity farMultiplicity) {
		System.out.println("+++ " + nearEntity.getName() + "." + nearPropertyName + " <-> " + farEntity.getName() + "." + farPropertyName);
		return new EntityReferenceEndpoint(nearEntity, nearPropertyName, nearMultiplicity, farEntity, farPropertyName, farMultiplicity);
	}

	/**
	 * Constructor.
	 */
	private EntityReferenceEndpoint(final EntityDescriptor nearEntity, final String nearPropertyName, final EntityReferenceEndpointMultiplicity nearMultiplicity, final EntityDescriptor farEntity, final String farPropertyName, final EntityReferenceEndpointMultiplicity farMultiplicity) {
		this.entity = nearEntity;
		this.propertyName = nearPropertyName;
		this.multiplicity = nearMultiplicity;
		this.other = new EntityReferenceEndpoint(farEntity, farPropertyName, farMultiplicity, this);
	}

	/**
	 * Constructor.
	 */
	private EntityReferenceEndpoint(final EntityDescriptor entity, final String propertyName, final EntityReferenceEndpointMultiplicity multiplicity, final EntityReferenceEndpoint other) {
		this.entity = entity;
		this.propertyName = propertyName;
		this.multiplicity = multiplicity;
		this.other = other;
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}

	/**
	 * Getter method for the propertyName.
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Getter method for the multiplicity.
	 * @return the multiplicity
	 */
	public EntityReferenceEndpointMultiplicity getMultiplicity() {
		return multiplicity;
	}
	
	/**
	 * Getter method for the other.
	 * @return the other
	 */
	public EntityReferenceEndpoint getOther() {
		return other;
	}

}
