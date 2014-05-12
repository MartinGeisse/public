/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.validation.impl;

import java.lang.annotation.Annotation;
import name.martingeisse.wicket.autoform.annotation.validation.AutoformValidator;
import org.apache.wicket.validation.IValidator;

/**
 * Ad-hoc implementation for {@link AutoformValidator}.
 */
public class AutoformValidatorImpl implements AutoformValidator {

	/**
	 * the value
	 */
	private final Class<? extends IValidator<?>>[] value;

	/**
	 * Constructor.
	 * @param value the value of this annotation
	 */
	public AutoformValidatorImpl(final Class<? extends IValidator<?>>[] value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformValidator.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.annotation.components.AutoformComponent#value()
	 */
	@Override
	public Class<? extends IValidator<?>>[] value() {
		return value;
	}

}
