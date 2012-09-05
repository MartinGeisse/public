/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.autoform;

import java.lang.annotation.Annotation;

import name.martingeisse.common.util.ClassKeyedContainer;

/**
 * This class stores per-property information for {@link EntityAutoformMetadata}.
 */
public final class EntityPropertyAutoformMetadata {

	/**
	 * the annotations
	 */
	private final ClassKeyedContainer<Annotation> annotations = new ClassKeyedContainer<Annotation>();
	
	/**
	 * Constructor.
	 */
	public EntityPropertyAutoformMetadata() {
	}

	/**
	 * Getter method for the annotations.
	 * @return the annotations
	 */
	public ClassKeyedContainer<Annotation> getAnnotations() {
		return annotations;
	}
	
}
