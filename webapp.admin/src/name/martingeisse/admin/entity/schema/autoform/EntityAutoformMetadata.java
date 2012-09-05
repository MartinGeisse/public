/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.autoform;

import java.lang.annotation.Annotation;

import name.martingeisse.common.util.ClassKeyedContainer;

/**
 * This class stores entity meta-data that is used to build autoforms.
 * The meta-data comes from pattern-oriented meta-data contributors
 * as well as (optionally) a single annotated class.
 * 
 * Note: Even if no useful information is available, this class must
 * at least add an empty meta-data object to each entity and
 * each property, so other code can rely on these objects being there.
 */
public final class EntityAutoformMetadata {

	/**
	 * the annotations
	 */
	private final ClassKeyedContainer<Annotation> annotations = new ClassKeyedContainer<Annotation>();
	
	/**
	 * Constructor.
	 */
	public EntityAutoformMetadata() {
	}

	/**
	 * Getter method for the annotations.
	 * @return the annotations
	 */
	public ClassKeyedContainer<Annotation> getAnnotations() {
		return annotations;
	}
	
}
