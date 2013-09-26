/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.entity;

import java.util.List;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;

import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * This model returns all entity descriptors. A shared instance of this class
 * is provided.
 */
public final class AllEntityDescriptorsModel extends AbstractReadOnlyModel<List<EntityDescriptor>> {

	/**
	 * The singleton instance of this class.
	 */
	public static final AllEntityDescriptorsModel instance = new AllEntityDescriptorsModel();

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public List<EntityDescriptor> getObject() {
		return ApplicationSchema.instance.getEntityDescriptors();
	}

}
