/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import java.util.List;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.lowlevel.ILowlevelDatabaseStructure;
import name.martingeisse.admin.entity.schema.lowlevel.JdbcColumnStructure;
import name.martingeisse.admin.entity.schema.lowlevel.JdbcForeignKey;
import name.martingeisse.admin.entity.schema.lowlevel.JdbcForeignKeyElement;
import name.martingeisse.admin.entity.schema.lowlevel.JdbcSchemaStructure;

/**
 * This implementation of {@link IEntityReferenceDetector} implements
 * default detection logic: It detects references from foreign key
 * constraints.
 */
public class DefaultEntityReferenceDetector extends AbstractEntityReferenceDetector {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.reference.IEntityReferenceDetector#detectEntityReference(name.martingeisse.admin.entity.schema.ApplicationSchema, name.martingeisse.admin.entity.schema.lowlevel.ILowlevelDatabaseStructure, name.martingeisse.admin.entity.schema.EntityDescriptor, java.lang.String)
	 */
	@Override
	public void detectEntityReference(ApplicationSchema schema, ILowlevelDatabaseStructure lowlevelDatabaseStructure, EntityDescriptor entity, String propertyName) {
		if (!(lowlevelDatabaseStructure instanceof JdbcSchemaStructure)) {
			return;
		}
		JdbcSchemaStructure database = (JdbcSchemaStructure)lowlevelDatabaseStructure;
		List<JdbcForeignKey> importedForeignKeys = database.getTablesByName().get(entity.getTableName()).getImportedForeignKeys();
		for (JdbcForeignKey key : importedForeignKeys) {
			if (key.getElements().size() == 1) {
				JdbcForeignKeyElement element = key.getElements().get(0);
				if (element.getForeignColumn().getColumn().equals(propertyName)) {
					
					// detect multiplicity by checking whether the foreign key field is nullable
					JdbcColumnStructure foreignKeyColumn = database.getTablesByName().get(entity.getTableName()).getColumnsByName().get(propertyName);
					EntityReferenceEndpointMultiplicity farMultiplicity = (foreignKeyColumn.isNullable() ? EntityReferenceEndpointMultiplicity.ZERO_OR_ONE : EntityReferenceEndpointMultiplicity.ONE);
					
					// build the reference
					EntityDescriptor farEntity = schema.findEntityByTableName(element.getParentColumn().getTable());
					String farPropertyName = element.getParentColumn().getColumn();
					EntityReferenceEndpoint near = EntityReferenceEndpoint.createPair(entity, propertyName, EntityReferenceEndpointMultiplicity.ANY, farEntity, farPropertyName, farMultiplicity);
					entity.getReferenceEndpoints().add(near);
					farEntity.getReferenceEndpoints().add(near.getOther());
					
				}
			}
		}
	}

}
