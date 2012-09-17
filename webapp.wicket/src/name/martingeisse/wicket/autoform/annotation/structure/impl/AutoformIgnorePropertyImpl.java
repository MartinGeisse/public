/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.structure.impl;

import java.lang.annotation.Annotation;

import name.martingeisse.wicket.autoform.annotation.structure.AutoformIgnoreProperty;

/**
 * Ad-hoc implementation for {@link AutoformIgnoreProperty}.
 */
public class AutoformIgnorePropertyImpl implements AutoformIgnoreProperty {

	/**
	 * Constructor.
	 */
	public AutoformIgnorePropertyImpl() {
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformIgnoreProperty.class;
	}

}
