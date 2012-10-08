/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.autoform;

import java.lang.annotation.Annotation;
import java.util.List;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper;

/**
 * Helper class for {@link EntityAutoformDescriber}.
 */
public class EntityAutoformDescriberHelper extends AbstractAutoformBeanDescriberHelper<EntityDescriptor, EntityPropertyDescriptor, EntityAutoformDescriber> {

	/**
	 * Constructor.
	 * @param describer the parent describer
	 * @param entityDescriptor the entity descriptor
	 */
	public EntityAutoformDescriberHelper(final EntityAutoformDescriber describer, final EntityDescriptor entityDescriptor) {
		super(describer, entityDescriptor);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getBeanAnnotation(java.lang.Object, java.lang.Class)
	 */
	@Override
	protected <A extends Annotation> A getBeanAnnotation(final EntityDescriptor entityDescriptor, final Class<A> annotationClass) {
		return entityDescriptor.getAnnotations().get(annotationClass);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getPropertyDescriptors(java.lang.Object)
	 */
	@Override
	protected EntityPropertyDescriptor[] getPropertyDescriptors(final EntityDescriptor entityDescriptor) {
		List<EntityPropertyDescriptor> properties = entityDescriptor.getProperties().getPropertiesInDatabaseOrder();
		return properties.toArray(new EntityPropertyDescriptor[properties.size()]);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getPropertyName(java.lang.Object)
	 */
	@Override
	protected String getPropertyName(final EntityPropertyDescriptor propertyDescriptor) {
		return propertyDescriptor.getName();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriberHelper#getPropertyAnnotation(java.lang.Object, java.lang.Class)
	 */
	@Override
	protected <A extends Annotation> A getPropertyAnnotation(final EntityPropertyDescriptor propertyDescriptor, final Class<A> annotationClass) {
		return propertyDescriptor.getAnnotations().get(annotationClass);
	}

}
