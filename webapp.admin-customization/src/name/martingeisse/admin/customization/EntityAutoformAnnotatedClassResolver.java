/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.autoform.IEntityAutoformAnnotatedClassResolver;
import name.martingeisse.common.util.string.StringUtil;

/**
 *
 */
public class EntityAutoformAnnotatedClassResolver implements IEntityAutoformAnnotatedClassResolver {

	/**
	 * the packageName
	 */
	private final String packageName;
	
	/**
	 * Constructor.
	 * @param packageName the name of the package that contains annotated classes
	 */
	public EntityAutoformAnnotatedClassResolver(final String packageName) {
		this.packageName = packageName;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.autoform.IEntityAutoformAnnotatedClassResolver#resolveEntityAutoformAnnotatedClass(name.martingeisse.admin.entity.schema.EntityDescriptor)
	 */
	@Override
	public Class<?> resolveEntityAutoformAnnotatedClass(EntityDescriptor entity) {
		String entityName = entity.getName();
		String simpleClassName = convertEntityNameToClassName(entityName);
		String fullyQualifiedClassName = packageName + '.' + simpleClassName;
		try {
			return Class.forName(fullyQualifiedClassName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	private static String convertEntityNameToClassName(String entityName) {
		String className = StringUtil.convertUnderscoresToUpperCamelCase(entityName);
		if (className.endsWith("s")) {
			return className.substring(0, className.length() - 1);
		} else {
			return className;
		}
	}

}
