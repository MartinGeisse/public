/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.autoform;

import java.util.List;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper;
import name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriptor;
import name.martingeisse.wicket.autoform.describe.DefaultAutoformBeanDescriber;
import name.martingeisse.wicket.autoform.describe.IAutoformPropertyDescriptor;

/**
 * This class is a variant {@link DefaultAutoformBeanDescriber} that finds
 * properties from entity descriptors and annotations from the
 * autoform meta-data stored in those descriptors.
 * 
 * The "beans" described by this object are {@link EntityInstance} objects.
 */
public class EntityAutoformDescriber extends AbstractAutoformBeanDescriber<EntityDescriptor, EntityPropertyDescriptor, EntityInstance> {

	/**
	 * the default instance of this class
	 */
	public static final EntityAutoformDescriber instance = new EntityAutoformDescriber();

	/**
	 * Constructor.
	 */
	public EntityAutoformDescriber() {
		super(EntityInstance.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber#createHelper(java.lang.Object)
	 */
	@Override
	protected AbstractAutoformBeanDescriberHelper<EntityDescriptor, EntityPropertyDescriptor, ?> createHelper(final EntityInstance entityInstance) {
		return new EntityAutoformDescriberHelper(this, entityInstance.getEntity());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber#createBeanDescriptor(java.lang.Object, java.util.List)
	 */
	@Override
	protected AbstractAutoformBeanDescriptor<EntityInstance> createBeanDescriptor(final EntityInstance entityInstance, final List<IAutoformPropertyDescriptor> propertyDescriptors) {
		return new EntityAutoformBeanDescriptor(entityInstance, propertyDescriptors);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber#createPropertyDescriptor(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected IAutoformPropertyDescriptor createPropertyDescriptor(final EntityInstance entityInstance, final EntityPropertyDescriptor propertyDescriptor) {
		return new EntityAutoformPropertyDescriptor(entityInstance, propertyDescriptor);
	}

}