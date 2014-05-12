/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Simple {@link IChoiceRenderer} implementation that uses the toString() method
 * for both the id and the visible value of each element.
 * @param <T> the element type
 */
public class ToStringChoiceRenderer<T> implements IChoiceRenderer<T> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	@Override
	public Object getDisplayValue(T object) {
		return object.toString();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
	 */
	@Override
	public String getIdValue(T object, int index) {
		return object.toString();
	}

}
