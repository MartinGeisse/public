/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.validation.palette.impl;

import java.lang.annotation.Annotation;

import name.martingeisse.wicket.autoform.annotation.validation.palette.MinStringLength;

/**
 * Ad-hoc implementation for {@link MinStringLength}.
 */
public class MinStringLengthImpl implements MinStringLength {

	/**
	 * the value
	 */
	private final int value;

	/**
	 * Constructor.
	 * @param value the value of this annotation
	 */
	public MinStringLengthImpl(final int value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return MinStringLength.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.annotation.components.AutoformComponent#value()
	 */
	@Override
	public int value() {
		return value;
	}

}
