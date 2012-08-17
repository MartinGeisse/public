/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.entity.schema.ApplicationSchema;

import org.apache.commons.lang.StringUtils;

/**
 * This implementation of {@link IEntityReferenceDetector} implements
 * default detection logic.
 */
public class DefaultEntityReferenceDetector extends AbstractEntityReferenceDetector {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.entity.schema.ApplicationSchema, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String detectEntityReference(final ApplicationSchema schema, final String entityName, final String entityTableName, final String propertyName) {
		if (propertyName.toLowerCase().endsWith("_id")) {
			return StringUtils.capitalize(propertyName.substring(0, propertyName.length() - 3));
		} else if (propertyName.endsWith("Id")) {
			return StringUtils.capitalize(propertyName.substring(0, propertyName.length() - 2));
		} else {
			return null;
		}
	}

}
