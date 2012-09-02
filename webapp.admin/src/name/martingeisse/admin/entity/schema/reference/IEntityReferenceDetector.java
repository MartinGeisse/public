/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import java.io.Serializable;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

/**
 * Implementations are contributed by plugins and are able to
 * recognize entity fields as references to other entities.
 */
public interface IEntityReferenceDetector extends Serializable {

	/**
	 * Checks if the specified property of the specified entity is a
	 * reference to another entity. If so, creates {@link EntityReferenceEndpoint}
	 * objects for both endpoints and adds them to the entities.
	 * 
	 * This detector should be careful to add each reference only once:
	 * It will be called for each property of each entity. The detector
	 * must avoid adding the reference twice, once for each endpoint.
	 * 
	 * @param schema the application schema. Note that this object must be used
	 * to look up entities and to add the endpoints since the global schema
	 * reference is not yet initialized when this method is invoked.
	 * @param entity the entity that contains the property
	 * @param propertyName the name of the property
	 */
	public void detectEntityReference(ApplicationSchema schema, EntityDescriptor entity, String propertyName);

}
