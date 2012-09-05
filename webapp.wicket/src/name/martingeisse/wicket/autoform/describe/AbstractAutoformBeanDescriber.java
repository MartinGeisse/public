/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.describe;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.wicket.autoform.annotation.structure.AutoformIgnoreProperty;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformPropertyOrder;

/**
 * This describer builds descriptions for bean properties with the following rules:
 * - properties for which acceptAsRelevant() returns false are considered
 *   irrelevant to autoforms in general. No property description is build
 *   from them, and including them in {@link AutoformPropertyOrder} is an error.
 * - properties for which acceptAsVisible() returns false are hidden in
 *   the description, although relevant in principle. They are also expected
 *   to appear in {@link AutoformPropertyOrder} (if present).
 *   This can be used to hide specific properties of a bean according to
 *   the context, using a context-specific describer subclass.
 * - properties tagged with {@link AutoformIgnoreProperty} do not produce
 *   a property description. Whether or not they appear in
 *   {@link AutoformPropertyOrder} is ignored. This allows the bean author to
 *   skip the distinction and hide a property with the same annotation,
 *   no matter if the property is irrelevant, relevant but permanently
 *   hidden, or temporarily hidden for quick prototyping.
 * 
 * Internals:
 * 
 * The describer starts with a list of all properties that is generated
 * in a subclass-specific manner, and first performs the following
 * validation check: If {@link AutoformPropertyOrder} is present, then
 * each property that is not tagged with {@link AutoformIgnoreProperty}
 * must appear in that order list if and only if it is relevant according
 * to acceptAsRelevant().
 * 
 * The describer then builds a property list: If {@link AutoformPropertyOrder}
 * is present, then the list is built from that order, skipping all properties
 * with {@link AutoformIgnoreProperty}. Otherwise it contains all properties
 * without {@link AutoformIgnoreProperty} in an unspecified order.
 * 
 * This describer then removes all hidden properties, i.e. properties for
 * which acceptAsVisible() returns false. For all remaining properties, a
 * property description is generated.
 * 
 * @param <BEANDESC> the type of implementation-specific bean descriptors being used
 * @param <PROPDESC> the type of implementation-specific property descriptors being used
 * @param <BEAN> the base class for accepted beans
 */
public abstract class AbstractAutoformBeanDescriber<BEANDESC, PROPDESC, BEAN> implements IAutoformBeanDescriber {

	/**
	 * the beanBaseClass
	 */
	private final Class<BEAN> beanBaseClass;
	
	/**
	 * Constructor.
	 * @param beanBaseClass the expected base class for all beans
	 */
	public AbstractAutoformBeanDescriber(Class<BEAN> beanBaseClass) {
		this.beanBaseClass = beanBaseClass;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.autoform.IAutoformBeanDescriber#describe(java.lang.Object)
	 */
	@Override
	public DefaultAutoformBeanDescription describe(Object untypedBean) {
		
		// analyze the bean
		BEAN bean = beanBaseClass.cast(untypedBean);
		List<PROPDESC> sortedVisibleBeanPropertyDescriptors = createHelper(bean).run();
		
		// generate our own property descriptors for the properties
		List<IAutoformPropertyDescription> propertyDescriptions = new ArrayList<IAutoformPropertyDescription>();
		for (PROPDESC beanPropertyDescriptor : sortedVisibleBeanPropertyDescriptors) {
			propertyDescriptions.add(createPropertyDescription(bean, beanPropertyDescriptor));
		}
		
		// generate the bean description
		return createBeanDescription(bean, propertyDescriptions);
		
	}
	
	/**
	 * Subclasses can override this method to control which properties are considered
	 * relevant to autoforms. See the description of this class for detailed
	 * information. In particular, properties excluded by this method must not
	 * be listed in {@link AutoformPropertyOrder} (if present).
	 * 
	 * @param beanPropertyDescriptor the descriptor for the property to check
	 * @return true to accept the property as relevant, false to consider it
	 * annotated with {@link AutoformIgnoreProperty} implicitly
	 */
	protected boolean acceptAsRelevant(PROPDESC beanPropertyDescriptor) {
		return true;
	}

	/**
	 * Subclasses can override this method to control which properties are visible
	 * in an autoform. This allows to hide properties from outside a bean, but
	 * the properties are still considered relevant to autoforms. In particular,
	 * properties excluded by this method must still be listed by
	 * {@link AutoformPropertyOrder} (if present).
	 * 
	 * The default implementation does not exclude any property, i.e. it always
	 * returns true.
	 * 
	 * @param beanPropertyDescriptor the descriptor for the property to check
	 * @return true to show, false to hide
	 */
	protected boolean acceptAsVisible(PROPDESC beanPropertyDescriptor) {
		return true;
	}
	
	/**
	 * @return the describer-helper
	 */
	protected abstract AbstractAutoformBeanDescriberHelper<BEANDESC, PROPDESC, ?> createHelper(BEAN bean);
	
	/**
	 * Actually creates the bean description.
	 * @param bean the bean
	 * @param propertyDescriptions the property descriptions
	 * @return the bean description
	 */
	protected abstract DefaultAutoformBeanDescription createBeanDescription(BEAN bean, List<IAutoformPropertyDescription> propertyDescriptions);

	/**
	 * Creates a description object for one of the bean's properties.
	 * @param bean the bean being described
	 * @param beanPropertyDescriptor the internal property descriptor
	 * @return the property description
	 */
	protected abstract IAutoformPropertyDescription createPropertyDescription(BEAN bean, PROPDESC beanPropertyDescriptor);
	
}
