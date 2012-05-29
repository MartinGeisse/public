/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.capabilities;

import name.martingeisse.admin.schema.AbstractApplicationSchema;

import org.apache.commons.lang.StringUtils;

/**
 * This implementation of {@link IEntityReferenceDetector} implements
 * default detection logic.
 */
public class DefaultEntityReferenceDetector implements IEntityReferenceDetector {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.schema.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.schema.AbstractApplicationSchema, java.lang.String, java.lang.String)
	 */
	@Override
	public String detectEntityReference(AbstractApplicationSchema schema, String entityName, String propertyName) {
		if (propertyName.toLowerCase().endsWith("_id")) {
			return StringUtils.capitalize(propertyName.substring(0, propertyName.length() - 3));
		} else if (propertyName.endsWith("Id")) {
			return StringUtils.capitalize(propertyName.substring(0, propertyName.length() - 2));
		} else {
			return null;
		}
	}
	
}
