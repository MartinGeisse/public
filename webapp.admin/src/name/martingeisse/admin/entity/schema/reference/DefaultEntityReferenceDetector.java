/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * This implementation of {@link IEntityReferenceDetector} implements
 * default detection logic.
 */
public class DefaultEntityReferenceDetector extends AbstractEntityReferenceDetector {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(DefaultEntityReferenceDetector.class);
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.entity.schema.ApplicationSchema, name.martingeisse.admin.entity.schema.EntityDescriptor, java.lang.String)
	 */
	@Override
	public void detectEntityReference(ApplicationSchema schema, EntityDescriptor entity, String propertyName) {
		
		// TODO re: camel-case, underscores, +"s", etc. -- is there any useful default behavior? use something simple and allow subclasses to override
		String destinationEntityName;
		if (propertyName.toLowerCase().endsWith("_id")) {
			// destinationEntityName = StringUtils.capitalize(propertyName.substring(0, propertyName.length() - 3));
			destinationEntityName = propertyName.substring(0, propertyName.length() - 3) + 's';
		} else if (propertyName.endsWith("Id")) {
			destinationEntityName = StringUtils.capitalize(propertyName.substring(0, propertyName.length() - 2));
		} else {
			return;
		}
		
		EntityDescriptor destination = schema.findEntity(destinationEntityName);
		if (destination == null) {
			logger.warn("entity reference in " + entity.getName() + "." + propertyName + " indicates an entity called " + destinationEntityName + ", but that entity doesn't exist");
			return;
		}

		EntityReferenceEndpoint near = EntityReferenceEndpoint.createPair(entity, propertyName, EntityReferenceEndpointMultiplicity.ANY, destination, destination.getIdColumnName(), EntityReferenceEndpointMultiplicity.ONE);
		entity.getReferenceEndpoints().add(near);
		destination.getReferenceEndpoints().add(near.getOther());

	}

}
