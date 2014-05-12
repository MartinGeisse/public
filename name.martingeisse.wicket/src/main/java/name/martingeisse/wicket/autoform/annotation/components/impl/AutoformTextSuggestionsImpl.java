/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation.components.impl;

import java.lang.annotation.Annotation;
import name.martingeisse.wicket.autoform.annotation.components.AutoformTextSuggestions;

/**
 * Ad-hoc implementation for {@link AutoformTextSuggestions}.
 */
public class AutoformTextSuggestionsImpl implements AutoformTextSuggestions {

	/**
	 * the value
	 */
	private final String[] value;

	/**
	 * Constructor.
	 * @param value the value of this annotation
	 */
	public AutoformTextSuggestionsImpl(final String[] value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see java.lang.annotation.Annotation#annotationType()
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AutoformTextSuggestions.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.autoform.annotation.components.AutoformComponent#value()
	 */
	@Override
	public String[] value() {
		return value;
	}

}
