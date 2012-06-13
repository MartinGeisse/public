/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.martingeisse.wicket.panel.simple.TextFieldPanel;
import name.martingeisse.wicket.panel.simple.TextFieldWithSuggestionsPanel;

/**
 * This annotation provides suggestions for a string-typed autoform
 * property and causes it to use a {@link TextFieldWithSuggestionsPanel}
 * instead of a {@link TextFieldPanel} by default.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformTextSuggestions {

	/**
	 * @return the suggestions
	 */
	public String[] value();
	
}
