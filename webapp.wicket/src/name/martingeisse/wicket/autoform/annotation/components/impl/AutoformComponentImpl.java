/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.components.impl;

import java.lang.annotation.Annotation;

import name.martingeisse.wicket.autoform.annotation.components.AutoformComponent;

import org.apache.wicket.Component;

/**
 * Ad-hoc implementation for {@link AutoformComponent}.
 */
public class AutoformComponentImpl implements AutoformComponent {

	/**
	 * the value
	 */
	private final Class<? extends Component> value;

	/**
	 * Constructor.
	 * @param value the value of this annotation
	 */
	public AutoformComponentImpl(final Class<? extends Component> value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformComponent.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.annotation.components.AutoformComponent#value()
	 */
	@Override
	public Class<? extends Component> value() {
		return value;
	}

}
