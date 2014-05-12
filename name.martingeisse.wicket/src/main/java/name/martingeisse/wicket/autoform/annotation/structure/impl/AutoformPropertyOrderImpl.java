/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.structure.impl;

import java.lang.annotation.Annotation;
import name.martingeisse.wicket.autoform.annotation.structure.AutoformPropertyOrder;

/**
 * Ad-hoc implementation for {@link AutoformPropertyOrder}.
 */
public class AutoformPropertyOrderImpl implements AutoformPropertyOrder {

	/**
	 * the value
	 */
	private final String[] value;

	/**
	 * Constructor.
	 * @param value the value of this annotation
	 */
	public AutoformPropertyOrderImpl(final String[] value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformPropertyOrder.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.annotation.components.AutoformComponent#value()
	 */
	@Override
	public String[] value() {
		return value;
	}

}
