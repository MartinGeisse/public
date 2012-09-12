/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.lang.annotation.Annotation;

import name.martingeisse.admin.entity.schema.type.ISqlTypeInfo;
import name.martingeisse.common.util.ClassKeyedContainer;

/**
 * This class describes a property of an entity, for example
 * a database column.
 */
public class EntityPropertyDescriptor {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the type
	 */
	private ISqlTypeInfo type;

	/**
	 * the visibleInRawEntityList
	 */
	private boolean visibleInRawEntityList;

	/**
	 * the annotations
	 */
	private ClassKeyedContainer<Annotation> annotations;

	/**
	 * Constructor.
	 */
	public EntityPropertyDescriptor() {
		this.annotations = new ClassKeyedContainer<Annotation>();
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public ISqlTypeInfo getType() {
		return type;
	}

	/**
	 * Setter method for the type.
	 * @param type the type to set
	 */
	public void setType(final ISqlTypeInfo type) {
		this.type = type;
	}

	/**
	 * Getter method for the visibleInRawEntityList.
	 * @return the visibleInRawEntityList
	 */
	public boolean isVisibleInRawEntityList() {
		return visibleInRawEntityList;
	}

	/**
	 * Setter method for the visibleInRawEntityList.
	 * @param visibleInRawEntityList the visibleInRawEntityList to set
	 */
	public void setVisibleInRawEntityList(final boolean visibleInRawEntityList) {
		this.visibleInRawEntityList = visibleInRawEntityList;
	}

	/**
	 * Getter method for the annotations.
	 * @return the annotations
	 */
	public ClassKeyedContainer<Annotation> getAnnotations() {
		return annotations;
	}

	/**
	 * Setter method for the annotations.
	 * @param annotations the annotations to set
	 */
	public void setAnnotations(final ClassKeyedContainer<Annotation> annotations) {
		this.annotations = annotations;
	}

}
