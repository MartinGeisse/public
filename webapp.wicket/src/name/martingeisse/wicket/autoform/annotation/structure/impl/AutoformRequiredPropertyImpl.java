/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.structure.impl;

import java.lang.annotation.Annotation;

import name.martingeisse.wicket.autoform.annotation.structure.AutoformRequiredProperty;

/**
 * Ad-hoc implementation for {@link AutoformRequiredProperty}.
 */
public class AutoformRequiredPropertyImpl implements AutoformRequiredProperty {

	/**
	 * Constructor.
	 */
	public AutoformRequiredPropertyImpl() {
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformRequiredProperty.class;
	}

}
