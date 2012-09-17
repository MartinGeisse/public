/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.validation.impl;

import java.lang.annotation.Annotation;

import name.martingeisse.wicket.autoform.annotation.validation.AutoformAssociatedValidator;

import org.apache.wicket.validation.IValidator;

/**
 * Ad-hoc implementation for {@link AutoformAssociatedValidator}.
 */
public class AutoformAssociatedValidatorImpl implements AutoformAssociatedValidator {

	/**
	 * the value
	 */
	private final Class<? extends IValidator<?>> value;

	/**
	 * Constructor.
	 * @param value the value of this annotation
	 */
	public AutoformAssociatedValidatorImpl(final Class<? extends IValidator<?>> value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformAssociatedValidator.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.annotation.components.AutoformComponent#value()
	 */
	@Override
	public Class<? extends IValidator<?>> value() {
		return value;
	}

}
