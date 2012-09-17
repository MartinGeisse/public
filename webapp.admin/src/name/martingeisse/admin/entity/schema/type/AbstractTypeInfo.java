/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

import java.lang.annotation.Annotation;

import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.wicket.autoform.annotation.structure.impl.AutoformRequiredPropertyImpl;

/**
 * Base implementation of {@link ITypeInfo}.
 */
public abstract class AbstractTypeInfo implements ITypeInfo {

	/**
	 * the nullable
	 */
	private final boolean nullable;
	
	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public AbstractTypeInfo(final boolean nullable) {
		this.nullable = nullable;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.ITypeInfo#isNullable()
	 */
	@Override
	public boolean isNullable() {
		return nullable;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.ITypeInfo#contributeImplicitAutoformAnnotations(java.util.List)
	 */
	@Override
	public void contributeImplicitAutoformAnnotations(IConsumer<Annotation> annotations) {
		if (!nullable) {
			// TODO: empty strings vs. null
			annotations.consume(new AutoformRequiredPropertyImpl());
		}
	}
	
}
