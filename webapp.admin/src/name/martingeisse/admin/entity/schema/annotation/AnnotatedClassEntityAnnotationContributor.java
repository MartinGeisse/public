/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import name.martingeisse.admin.entity.schema.ApplicationSchema;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.EntityPropertyDescriptor;
import name.martingeisse.common.util.ClassKeyedContainer;
import name.martingeisse.common.util.string.StringUtil;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Default implementation of {@link IEntityAnnotationContributor}.
 * This class copies annotations from an annotated class that
 * mirrors a database table. Annotations at the class are used to
 * control entity-level handling. Public fields as well as bean
 * properties are used to control property-level handling (property
 * annotations are expected either at a public field or at a getter
 * method, with the field name or bean property name being a
 * camel-cased version of the entity property name). Annotations
 * at non-public fields, non-public getter methods, getter methods
 * with parameters, or setter methods are ignored!
 */
public final class AnnotatedClassEntityAnnotationContributor implements IEntityAnnotationContributor {

	/**
	 * the annotatedClassResolver
	 */
	private final IEntityAnnotatedClassResolver annotatedClassResolver;
	
	/**
	 * Constructor.
	 * @param annotatedClassResolver the annotated class resolver
	 */
	public AnnotatedClassEntityAnnotationContributor(IEntityAnnotatedClassResolver annotatedClassResolver) {
		this.annotatedClassResolver = annotatedClassResolver;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.annotation.IEntityAnnotationResolver#resolveEntityAnnotations(name.martingeisse.admin.entity.schema.ApplicationSchema)
	 */
	@Override
	public void contributeEntityAnnotations(ApplicationSchema applicationSchema) {
		for (final EntityDescriptor entity : applicationSchema.getEntityDescriptors()) {

			// find the annotated class for this entity (if any)
			final Class<?> annotatedClass = annotatedClassResolver.resolveEntityAnnotatedClass(entity);
			if (annotatedClass == null) {
				continue;
			}
			
			// copy entity-level annotations
			copy(annotatedClass.getAnnotations(), entity.getAnnotations());

			// copy annotations from public fields
			for (final Field field : annotatedClass.getFields()) {
				if ((field.getModifiers() & Modifier.PUBLIC) != 0) {
					final String name = StringUtil.convertCamelCaseToLowercaseUnderscores(field.getName());
					copy(field.getAnnotations(), entity.getProperties().get(name).getAnnotations());
				}
			}

			// copy annotations from public bean getters
			for (final PropertyDescriptor beanPropertyDescriptor : PropertyUtils.getPropertyDescriptors(annotatedClass)) {
				if (beanPropertyDescriptor.getName().equals("class")) {
					continue;
				}
				final Method getter = beanPropertyDescriptor.getReadMethod();
				if ((getter.getModifiers() & Modifier.PUBLIC) != 0) {
					final String entityPropertyName = StringUtil.convertCamelCaseToLowercaseUnderscores(beanPropertyDescriptor.getName());
					final EntityPropertyDescriptor entityPropertyDescriptor = entity.getProperties().get(entityPropertyName);
					if (entityPropertyDescriptor == null) {
						throw new RuntimeException("bean property " + beanPropertyDescriptor.getName() + " of the annotated entity class " + annotatedClass +
							" has no corresponding entity property; tried: " + entityPropertyName);
					}
					copy(getter.getAnnotations(), entityPropertyDescriptor.getAnnotations());
				}
			}

		}
	}

	/**
	 * 
	 */
	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	private static void copy(final Annotation[] source, final ClassKeyedContainer<Annotation> destination) {
		final ClassKeyedContainer untypedDestination = destination;
		for (final Annotation annotation : source) {
			final Class<? extends Annotation> annotationType = annotation.annotationType();
			untypedDestination.set(annotationType, annotationType.cast(annotation));
		}
	}

}
