/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

/**
 * This class contains meta-data about a specific generated entity
 * class. An instance is generated for each generated class and
 * passed to generic code.
 */
public final class SpecificEntityInstanceMeta {

	/**
	 * the concreteClass
	 */
	private final Class<?> concreteClass;
	
	/**
	 * Constructor.
	 * @param concreteClass the concrete class of the entity instance
	 */
	public SpecificEntityInstanceMeta(Class<?> concreteClass) {
		this.concreteClass = concreteClass;
	}
	
	/**
	 * Getter method for the concreteClass.
	 * @return the concreteClass
	 */
	public Class<?> getConcreteClass() {
		return concreteClass;
	}
	
}
