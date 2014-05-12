/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.components.impl;

import java.lang.annotation.Annotation;
import name.martingeisse.wicket.autoform.annotation.components.AutoformComponentConstructorArgument;

/**
 * Ad-hoc implementation for {@link AutoformComponentConstructorArgument}.
 */
public class AutoformComponentConstructorArgumentImpl implements AutoformComponentConstructorArgument {

	/**
	 * the value
	 */
	private final Class<? extends Annotation> value;

	/**
	 * Constructor.
	 * @param value the value of this annotation
	 */
	public AutoformComponentConstructorArgumentImpl(final Class<? extends Annotation> value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformComponentConstructorArgument.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.annotation.components.AutoformComponent#value()
	 */
	@Override
	public Class<? extends Annotation> value() {
		return value;
	}

}
