/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;

/**
 * This class is the default concrete subclass of {@link AbstractAutoformBeanDescriber}.
 * It makes the following rules concrete:
 * - annotations are taken from the bean class and bean properties.
 *   Property annotations must be attached to getter methods.
 * - acceptAsRelevant() excludes the "class" property by default
 *   (i.e. Java's getClass() method).
 * - acceptAsVisible() does not exclude any properties by default.
 */
public class DefaultAutoformBeanDescriber extends AbstractAutoformBeanDescriber<Class<? extends Serializable>, PropertyDescriptor, Serializable> {

	/**
	 * the default instance of this class
	 */
	public static final DefaultAutoformBeanDescriber instance = new DefaultAutoformBeanDescriber();

	/**
	 * Constructor.
	 */
	public DefaultAutoformBeanDescriber() {
		super(Serializable.class);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber#createHelper(java.lang.Object)
	 */
	@Override
	protected AbstractAutoformBeanDescriberHelper<Class<? extends Serializable>, PropertyDescriptor, ?> createHelper(final Serializable bean) {
		return new DefaultAutoformBeanDescriberHelper(this, bean.getClass());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber#createBeanDescription(java.lang.Object, java.util.List)
	 */
	@Override
	protected DefaultAutoformBeanDescription createBeanDescription(final Serializable bean, final List<IAutoformPropertyDescription> propertyDescriptions) {
		return new DefaultAutoformBeanDescription(bean, propertyDescriptions);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber#createPropertyDescription(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected IAutoformPropertyDescription createPropertyDescription(final Serializable bean, final PropertyDescriptor beanPropertyDescriptor) {
		return new DefaultAutoformPropertyDescription(bean, beanPropertyDescriptor);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.describe.AbstractAutoformBeanDescriber#acceptAsRelevant(java.lang.Object)
	 */
	@Override
	protected boolean acceptAsRelevant(final PropertyDescriptor beanPropertyDescriptor) {
		return (super.acceptAsRelevant(beanPropertyDescriptor) && !"class".equals(beanPropertyDescriptor.getName()));
	}

}
